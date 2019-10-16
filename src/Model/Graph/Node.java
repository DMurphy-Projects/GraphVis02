package Model.Graph;

import Model.Chunk;

public class Node {

    private static int ID = 0;

    private int id;

    int x, y;
    float rX, rY;

    Chunk belongsTo;

    public Node(int x, int y)
    {
        this.x = x;
        this.y = y;

        this.id = Node.ID++;
    }

    private void updateRelative()
    {
        int size = belongsTo.getSize();
        rX = this.x % size;
        rY = this.y % size;
    }

    public void belongsTo(Chunk c)
    {
        this.belongsTo = c;
        updateRelative();
    }

    public Chunk getBelongsTo() {
        return belongsTo;
    }

    public void move(int x, int y)
    {
        this.x += x;
        this.y += y;

        updateRelative();
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public float getRelativeX() {
        return rX;
    }

    public float getRelativeY() {
        return rY;
    }

    public int ID()
    {
        return id;
    }
}
