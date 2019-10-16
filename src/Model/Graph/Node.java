package Model.Graph;

import Model.Chunk;

import java.awt.*;

public class Node {

    private static int ID = 0;

    private int id;

    int x, y, dx, dy;
    float rX, rY;

    int diam = 2, rad;

    Chunk belongsTo;

    public Node(int x, int y)
    {
        this.x = x;
        this.y = y;

        rad = diam / 2;

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

    public void draw(Graphics g, int x, int y)
    {
        this.dx = x + belongsTo.getDrawX();
        this.dy = y + belongsTo.getDrawY();

        g.drawOval(dx - rad, dy - rad, diam, diam);
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

    public int getDrawX() {
        return dx;
    }

    public int getDrawY() {
        return dy;
    }

    public int ID()
    {
        return id;
    }
}
