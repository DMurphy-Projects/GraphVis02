package Model.Graph;

public class Relationship {

    Node n1, n2;

    public Relationship(Node n1, Node n2)
    {
        this.n1 = n1;
        this.n2 = n2;
    }

    public Node getN1() {
        return n1;
    }

    public Node getN2() {
        return n2;
    }
}
