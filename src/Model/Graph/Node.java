package Model.Graph;

import Model.Chunk;

public class Node {

    private static int ID = 0;

    private int id;

    protected double x, y;

    Chunk belongsTo;

    public Node(int x, int y)
    {
        this.x = x;
        this.y = y;

        this.id = Node.ID++;
    }

    public void belongsTo(Chunk c)
    {
        this.belongsTo = c;
    }

    public Chunk getBelongsTo() {
        return belongsTo;
    }

    public void move(double x, double y)
    {
        this.x += x;
        this.y += y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public int ID()
    {
        return id;
    }
}
