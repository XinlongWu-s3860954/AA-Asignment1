import java.io.PrintWriter;
import java.util.Arrays;


/**
 * Adjacency matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class AdjacencyMatrix extends AbstractGraph
{

    private NodeList matrixNodes;
    private int[][] matrix;

	/**
	 * Contructs empty graph.
	 */
    public AdjacencyMatrix() {
    	// Implement me!
        matrixNodes = new NodeList();
        matrix = new int[matrixNodes.getLength()][matrixNodes.getLength()];
    } // end of AdjacencyMatrix()


    public void addVertex(String vertLabel) {
        // Implement me!
        matrixNodes.addNode(vertLabel);
        int matrixSize = matrixNodes.getLength();

        int newMartix[][] = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize - 1; i++) {
            for (int j = 0; j < matrixSize - 1; j++) {
                newMartix[i][j] = matrix[i][j];
            }
        }
        for (int i = 0; i < matrixSize; i++) {
            newMartix[matrixSize-1][i] = 0;
            newMartix[i][matrixSize-1] = 0;
        }

        matrix = newMartix;
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        // Implement me!
        int srcPos = matrixNodes.getNodeIndex(srcLabel);
        int tarPos = matrixNodes.getNodeIndex(tarLabel);

        matrix[srcPos][tarPos] = 1;
        matrix[tarPos][srcPos] = 1;
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        // Implement me!
        matrixNodes.getNode(vertLabel).toggleSIRstate();
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
        // Implement me!
        int srcPos = matrixNodes.getNodeIndex(srcLabel);
        int tarPos = matrixNodes.getNodeIndex(tarLabel);

        matrix[srcPos][tarPos] = 0;
        matrix[tarPos][srcPos] = 0;
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        // Implement me!
        int nodePos = matrixNodes.getNodeIndex(vertLabel);
        if(nodePos<0){
            return;
        }
        matrixNodes.deleteNode(vertLabel);
        int matrixSize = matrixNodes.getLength();

        for (int i = 0; i < matrixSize+1; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if(j>=nodePos){
                    matrix[i][j] = matrix[i][j+1];
                }
            }
            matrix[i] = Arrays.copyOf(matrix[i],matrixSize);
        }
        for (int i = 0; i < matrixSize; i++) {
            if(i>=nodePos){
                matrix[i] = matrix[i+1];
            }
        }
        matrix = Arrays.copyOf(matrix,matrixSize);
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        // Implement me!
        String res[];
        if(k==0){
            res = new String[]{" "};
        }
        else{
            res = new String[0];
            int index = matrixNodes.getNodeIndex(vertLabel);
            for (int i = 0; i < matrix[index].length; i++) {
                if(matrix[index][i]==1){
                    res = Arrays.copyOf(res,res.length+1);
                    res[res.length-1] = matrixNodes.getNode(i).getName();
                    if(k>1){
                        String tmp[] = kHopNeighbours(k-1,res[res.length-1]);
                        res = Arrays.copyOf(res,res.length+tmp.length);
                        for (int j = 0; j < tmp.length; j++) {
                            res[res.length-tmp.length+j] = tmp[j];
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

        return res;
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        // Implement me!
        int size = matrixNodes.getLength();
        for (int i = 0; i < size; i++) {
            Node node = matrixNodes.getNode(i);
            os.print("("+node.getName()+","+node.getSIRstate()+")");
            if (i!=size-1){
                os.print(" ");
            }
        }
        os.println();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        // Implement me!
        int size = matrixNodes.getLength();
        for (int i = 0; i < size; i++) {
            Node node = matrixNodes.getNode(i);
            for (int j = 0; j < size; j++) {
                if(matrix[i][j]==1){
                    os.println(node.getName()+" "+matrixNodes.getNode(j).getName());
                }
            }
        }
    } // end of printEdges()

} // end of class AdjacencyMatrix
