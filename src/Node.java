public class Node {
    public static final String SIR_S = "S";
    public static final String SIR_I = "I";
    public static final String SIR_R = "R";
    private String nodeName;
    private String SIRstate;
    public Node() {
        SIRstate = Node.SIR_S;
    }

    public Node(String nodeName) {
        this.nodeName = nodeName;
        SIRstate = Node.SIR_S;
    }

    public String getName() {
        return nodeName;
    }

    public void setName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSIRstate() {
        return SIRstate;
    }

    public void setSIRstate(String SIRstate) {
        this.SIRstate = SIRstate;
    }
}
