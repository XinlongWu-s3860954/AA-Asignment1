import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Incidence matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class IncidenceMatrix extends AbstractGraph
{

    private NodeList edgeLisgt;
    private NodeList vertexList;
    private int[][] matrix;

	/**
	 * Contructs empty graph.
	 */
    public IncidenceMatrix() {
    	// Implement me!
        edgeLisgt = new NodeList();
        vertexList = new NodeList();
        matrix = new int[vertexList.getLength()][edgeLisgt.getLength()];
    } // end of IncidenceMatrix()


    public void addVertex(String vertLabel) {
        // Implement me!
        int vertexNumber = vertexList.getLength();
        vertexList.addNode(vertLabel);
        matrix = Arrays.copyOf(matrix,vertexNumber+1);

        int edgeNumber = edgeLisgt.getLength();
        matrix[vertexNumber] = new int[edgeNumber];
        for (int i = 0; i < edgeNumber; i++) {
            matrix[vertexNumber][i] = 0;
        }
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        // Implement me!
        int vertexNumber = vertexList.getLength();
        int edgeNumber = edgeLisgt.getLength();
        for (int i = 0; i < vertexNumber; i++) {
            matrix[i] = Arrays.copyOf(matrix[i],edgeNumber+1);
        }

        edgeLisgt.addNode(srcLabel+" "+tarLabel);
        int index = vertexList.getNodeIndex(srcLabel);
        matrix[index][edgeNumber] = 1;
        index = vertexList.getNodeIndex(tarLabel);
        matrix[index][edgeNumber] = 1;
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        // Implement me!
        vertexList.getNode(vertLabel).toggleSIRstate();
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
        // Implement me!
        int vertexNumber = vertexList.getLength();
        int edgeNumber = edgeLisgt.getLength();

        int index = edgeLisgt.getNodeIndex(srcLabel+" "+tarLabel);
        if(index<0){
            index = edgeLisgt.getNodeIndex(tarLabel+" "+srcLabel);
            edgeLisgt.deleteNode(tarLabel+" "+srcLabel);
        }else {
            edgeLisgt.deleteNode(srcLabel+" "+tarLabel);
        }

        if(index>=0){
            for (int i = 0; i < vertexNumber; i++) {
                for (int j = 0; j < edgeNumber-1; j++) {
                    if(j>=index){
                        matrix[i][j] = matrix[i][j+1];
                    }
                }
                matrix[i] = Arrays.copyOf(matrix[i],edgeNumber-1);
            }
        }
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        // Implement me!
        int index = vertexList.getNodeIndex(vertLabel);
        int vertexNumber = vertexList.getLength();

        String nodeToDel[] = kHopNeighbours(1,vertLabel);
        for (String target : nodeToDel) {
            deleteEdge(vertLabel,target);
        }

        vertexList.deleteNode(vertLabel);
        for (int i = 0; i < vertexNumber-1; i++) {
            if(i>=index){
                matrix[i] = matrix[i+1];
            }
        }
        matrix = Arrays.copyOf(matrix,vertexNumber-1);
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        // Implement me!
        String res[];
        if(k==0){
            res = new String[]{" "};
        }else{
            res = new String[0];
            int index = vertexList.getNodeIndex(vertLabel);
            for (int i = 0; i < edgeLisgt.getLength(); i++) {
                if(matrix[index][i]==1){
                    res = Arrays.copyOf(res,res.length+1);
                    String tmp[] = edgeLisgt.getNode(i).getName().split(" ");
                    res[res.length-1] = tmp[0].equals(vertLabel)?tmp[1]:tmp[0];
                    if(k>1){
                        String tmpp[] = kHopNeighbours(k-1,res[res.length-1]);
                        res = Arrays.copyOf(res,res.length+tmpp.length);
                        for (int j = 0; j < tmpp.length; j++) {
                            res[res.length-tmpp.length+j] = tmpp[j];
                        }
                    }
                }
            }
            NodeList nodeList = new NodeList();
            for (String node : res) {
                if(!node.equals(vertLabel)&&nodeList.getNodeIndex(node)<0){
                    nodeList.addNode(node);
                }
            }
            res = nodeList.getAllNodeName();
        }
        // please update!
        return res;
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        // Implement me!
        int size = vertexList.getLength();
        for (int i = 0; i < size; i++) {
            Node node = vertexList.getNode(i);
            os.print("("+node.getName()+","+node.getSIRstate()+")");
            if (i!=size-1){
                os.print(" ");
            }
        }
        os.println();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        // Implement me!
        int size = edgeLisgt.getLength();
        for (int i = 0; i < size; i++) {
            String edges[] = edgeLisgt.getNode(i).getName().split(" ");
            os.println(edges[0]+" "+edges[1]);
            os.println(edges[1]+" "+edges[0]);
        }
    } // end of printEdges()

} // end of class IncidenceMatrix
