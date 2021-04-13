public class EdgeList {
    private int length;
    private Node[] list;
    private int maxListSize = 10;
    private Node vertex;

    public EdgeList() {
        length = 0;
        list = new Node[maxListSize];
        vertex = new Node();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Node getVertex() {
        return vertex;
    }

    public void setVertex(Node vertex) {
        this.vertex = vertex;
    }

    public Node getNode(int index) {
        return list[index];
    }

    public Node getNode(String nodeName) {
        int index = getNodeIndex(nodeName);
        if (index>=0){
            return list[index];
        }else {
            return null;
        }
    }

    public int getNodeIndex(String nodeName) {
        for (int i = 0; i < length; i++) {
            if(list.equals(list[i].getName())){
                return i;
            }
        }
        return -1;
    }

    public void addNode(String nodeName) {
        if(length>=maxListSize){
            resizeList();
        }
        list[length++].setName(nodeName);
    }

    public void deleteNode(String nodeName){
        int index = getNodeIndex(nodeName);
        if(index>=0){
            list[index] = null;
        }
        for (int i = 0; i < length-1; i++) {
            if(list[i]==null){
                list[i] = list[i+1];
                list[i+1] = null;
            }
        }
        length--;
    }

    private void resizeList(){
        maxListSize *= 2;
        Node[] newList = new Node[maxListSize];
        for (int i = 0; i < length; i++) {
            newList[i] = list[i];
        }
        list = newList;
    }


}
