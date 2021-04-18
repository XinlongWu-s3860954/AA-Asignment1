import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;

public class Evaluator {

	public static ContactsGraph randomGen(int numV, String type, double p) {
		ContactsGraph graph = null;
		Random r = new Random(10);
		switch(type) {
		case "adjlist":
			graph = new AdjacencyList();
			break;
		case "adjmat":
			graph = new AdjacencyMatrix();
			break;
		case "incmat":
			graph = new IncidenceMatrix();
			break;
		default:
			System.err.println("Unknown implmementation type.");
			return graph;
		}
		for(int i=0; i<numV; i++) {
			graph.addVertex("V"+i);
		}
		for(int i=0; i<numV; i++) {
			for(int j=i+1;j<numV; j++) {
				if (i==j) {
					continue;
				}
				if (r.nextDouble() <= p) {
					graph.addEdge("V"+i, "V"+j);
				}
			}
		}
		return graph;
	}
	
	private static ContactsGraph scaleFreeGen(int numV, String type) {
		// based on the description from Barabasi-Albert model

		ContactsGraph graph = null;
		Random r = new Random();
		switch(type) {
		case "adjlist":
			graph = new AdjacencyList();
			break;
		case "adjmat":
			graph = new AdjacencyMatrix();
			break;
		case "incmat":
			graph = new IncidenceMatrix();
			break;
		default:
			System.err.println("Unknown implmementation type.");
			return graph;
		}
		// init full connected graph
		int m0 = (int) (numV * 0.1);
		for(int i=0; i<m0; i++) {
			graph.addVertex("V"+i);
		}
		
		for(int i=0; i<m0; i++) {
			for(int j=i+1;j<m0; j++) {
				graph.addEdge("V"+i, "V"+j);
			}
		}
		// append the following
		int[] degree = new int[numV];
		for(int i=m0; i<numV; i++) {
			graph.addVertex("V"+i);
			int sum = 0;
			for(int j=0; j<i; j++) {
				degree[j] = graph.kHopNeighbours(1, "V"+j).length;
				System.out.print(degree[j] + " ");
				sum = sum + degree[j];
			}
//			System.out.println(sum);
			int count = 0;
			for(int j=0; j<i; j++) {
				if (r.nextDouble() <= ((double)(degree[j])/sum)) {
					graph.addEdge("V"+i, "V"+j);
					count = count + 1;
					if(count > m0) {
						break;
					}
				}
			}
		}
		return graph;
	}

	public static void printErrorMsg(String msg) {
		System.err.println("> " + msg);
	} // end of printErrorMsg()
	
	public static void printErrorMsg(PrintWriter outWriter, String msg) {
		outWriter.println("> " + msg);
	} // end of printErrorMsg()

	
	public static void processOperations(BufferedReader inReader, ContactsGraph graph, SIRModel sirModel,
			PrintWriter outWriter)
		throws IOException
	{
		String line;
		int lineNum = 1;
		boolean bQuit = false;

		// continue reading in commands until we either receive the quit signal or there are no more input commands
		while (!bQuit && (line = inReader.readLine()) != null) {
			String[] tokens = line.split(" ");

			// check if there is at least an operation command
			if (tokens.length < 1) {
				printErrorMsg("not enough tokens.");
				continue;
			}

			String command = tokens[0];

			try {
				// determine which operation to execute
				switch (command.toUpperCase()) {
					// add vertex
					case "AV":
						if (tokens.length == 2) {
							graph.addVertex(tokens[1]);
						}
						else {
							printErrorMsg("incorrect number of tokens.");
						}
						break;
	                // add edge
					case "AE":
						if (tokens.length == 3) {
							graph.addEdge(tokens[1], tokens[2]);
						}
						else {
							printErrorMsg("incorrect number of tokens.");
						}
						break;
					// toogle SIR state of vertex
					case "TV":
						if (tokens.length == 2) {
							graph.toggleVertexState(tokens[1]);
						}
						else {
							printErrorMsg("incorrect number of tokens.");
						}
						break;
					// remove/delete vertex
					case "DV":
						if (tokens.length == 2) {
							graph.deleteVertex(tokens[1]);
						}
						else {
							printErrorMsg("incorrect number of tokens.");
						}
						break;
					// remove/delete edge
					case "DE":
						if (tokens.length == 3) {
							graph.deleteEdge(tokens[1], tokens[2]);
						}
						else {
							printErrorMsg("incorrect number of tokens.");
						}
						break;
					// k-nearest neighbourhood
					case "KN":
						outWriter.println("# " + line);
						if (tokens.length == 3) {
							int k = Integer.parseInt(tokens[1]);
							if (k < 0) {
								printErrorMsg("k should be 0 or greater");
							}
							else {
								String[] neighbours = graph.kHopNeighbours(k, tokens[2]);

								outWriter.println(tokens[2] + ": " + String.join(" ", neighbours));
							}
						}
						else {
							printErrorMsg("incorrect number of tokens.");
						}

						break;
					// print vertices
					case "PV":
						outWriter.println("# " + line);
						graph.printVertices(outWriter);
						break;
	                // print edges
					case "PE":
						outWriter.println("# " + line);
						graph.printEdges(outWriter);
						break;
					// run SIR model
					case "SIR":
						outWriter.println("# " + line);
						if (tokens.length == 4) {
							String[] seedVertices = tokens[1].split(";");
							float infectionProb = Float.parseFloat(tokens[2]);
							float recoverProb = Float.parseFloat(tokens[3]);

							sirModel.runSimulation(graph, seedVertices, infectionProb, recoverProb, outWriter);
						}
						else {
							printErrorMsg("incorrect number of tokens.");
						}

						break;
					// quit
					case "Q":
						bQuit = true;
						break;
					default:
						printErrorMsg("Unknown command.");
				} // end of switch()
			}
			catch (IllegalArgumentException e) {
				printErrorMsg(e.getMessage());
			}

			lineNum++;
		}

	} // end of processOperations()


	public static void main(String[] args) {
		int numV = 100;
		double p = 0.5;
		
		String outFilename = "eva.out";
		// construct SIRs model
		SIRModel sirModel = new SIRModel();
		
		// Random
		ContactsGraph graph = randomGen(numV, "adjlist", p);
		System.out.println("Random done");
		// Scale-free
		ContactsGraph graph_free = scaleFreeGen(numV, "adjlist");
		System.out.println("Scale-free done");
		graph_free.printVertices(new PrintWriter(System.out, true));
		graph_free.printEdges(new PrintWriter(System.out, true));
		
		// construct in and output streams/writers/readers, then process each operation.
		try {
			BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
			// check if we need to redirect output to file
			PrintWriter outWriter = new PrintWriter(System.out, true);
			if (outFilename != null) {
				outWriter = new PrintWriter(new FileWriter(outFilename), true);
			}
			// process the operations
			processOperations(inReader, graph, sirModel, outWriter);
			System.out.println("Process Random done");
			processOperations(inReader, graph_free, sirModel, outWriter);
			System.out.println("Process graph_free done");
		} catch (IOException e) {
			printErrorMsg(e.getMessage());
		}
	}

}