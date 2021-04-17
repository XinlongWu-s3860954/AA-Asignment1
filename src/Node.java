public class Node {
    private String nodeName;
    private SIRState SIRstate;
    public Node() {
        SIRstate = SIRState.S;
    }

    public Node(String nodeName) {
        this.nodeName = nodeName;
        SIRstate = SIRState.S;
    }

    public String getName() {
        return nodeName;
    }

    public void setName(String nodeName) {
        this.nodeName = nodeName;
    }

    public SIRState getSIRstate() {
        return SIRstate;
    }

    public void setSIRstate(SIRState SIRstate) {
        this.SIRstate = SIRstate;
    }

    public void toggleSIRstate() {
        switch (getSIRstate().name()){
            case ("S"):
                this.SIRstate = SIRState.I;
                break;
            case ("I"):
                this.SIRstate = SIRState.R;
                break;
        }
    }
}
