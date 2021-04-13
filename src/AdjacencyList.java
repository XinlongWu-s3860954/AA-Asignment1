import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
// import java.util.*;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph
{
    private int maxSizeOfVertex = 10;
    private NodeList vertexList[];
    private int length;
    private int deleteCounter;

    /**
	 * Contructs empty graph.
	 */
    public AdjacencyList() {
    	 // Implement me!
        vertexList = new NodeList[maxSizeOfVertex];
        length = 0;
        deleteCounter = 0;
    } // end of AdjacencyList()


    public void addVertex(String vertLabel) {
        // Implement me!
        int index = length++;
        if(index>=maxSizeOfVertex){
            resizeVertexList();
        }
        vertexList[index] = new NodeList(vertLabel);
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        // Implement me!
        int index = getVertexIndex(srcLabel);
        if(index>=0){
            vertexList[index].addNode(tarLabel);
            if(getVertex(tarLabel).getNodeIndex(srcLabel)<0){
                addEdge(tarLabel,srcLabel);
            }
        }
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        // Implement me!
        getVertex(vertLabel).getVertex().toggleSIRstate();
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
        // Implement me!
        int index = getVertexIndex(srcLabel);
        if(index>=0){
            vertexList[index].deleteNode(tarLabel);
            getVertex(tarLabel).deleteNode(srcLabel);
        }
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        // Implement me!
        int index = getVertexIndex(vertLabel);
        if(index>=0){
            vertexList[index] = null;
            for (int i = 0; i < length-1; i++) {
                if(vertexList[i]==null){
                    vertexList[i] = vertexList[i+1];
                    vertexList[i+1] = null;
                }
                vertexList[i].deleteNode(vertLabel);
            }
            length--;
        }
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        // Implement me!
        NodeList vertex = getVertex(vertLabel);
        int width = vertex.getLength();
        String kHopneighbours[] = vertex.getAllNodeName(vertLabel);
        if(k==0){
            return new String[0];
        }else if(k>1){
            String neighbours[];
            for (String vt: kHopneighbours) {
                neighbours = kHopNeighbours(k-1,vt);
                kHopneighbours = Arrays.copyOf(kHopneighbours,neighbours.length + kHopneighbours.length);
                for (int i = 0; i < neighbours.length; i++) {
                    kHopneighbours[kHopneighbours.length-neighbours.length+i] = neighbours[i];
                }
            }
            Set set = new HashSet(Arrays.asList(kHopneighbours));
            kHopneighbours = (String [])set.toArray(new String[0]);
            kHopneighbours = Arrays.copyOfRange(kHopneighbours,1,kHopneighbours.length);
        }

        return kHopneighbours;
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        // Implement me!
        for (int i = 0; i < length; i++) {
            Node vertex = vertexList[i].getVertex();
            os.print("("+vertex.getName()+","+vertex.getSIRstate()+")");
        };
        os.println();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        // Implement me!
        for (int i = 0; i < length; i++) {
            NodeList edges = vertexList[i];
            Node vertex = edges.getVertex();
            for (int j = 0; j < edges.getLength(); j++) {
                os.println(vertex.getName()+" "+edges.getNode(j).getName());
            }
        }
    } // end of printEdges()

    private NodeList getVertex(String vertexName){
        int index = getVertexIndex(vertexName);
        if (index>=0){
            return vertexList[index];
        }else {
            return null;
        }
    }

    private int getVertexIndex(String vertexName){
        for (int i = 0; i < length; i++) {
            if(vertexName.equals(vertexList[i].getVertex().getName())){
                return i;
            }
        }
        return -1;
    }

    private void resizeVertexList(){
        maxSizeOfVertex *= 2;
        NodeList[] newVertexList = new NodeList[maxSizeOfVertex];
        for (int i = 0; i < length; i++) {
            newVertexList[i] = vertexList[i];
        }
        vertexList = newVertexList;
    }
} // end of class AdjacencyList
