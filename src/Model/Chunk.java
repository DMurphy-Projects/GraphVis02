package Model;

import Model.Graph.Node;
import Model.Graph.ViewNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Chunk{

    private String id;

    ArrayList<Node> nodes = new ArrayList<>();

    int rX, rY, size, x, y;

    int dx, dy, dw, dh;

    boolean visible;

    public Chunk(int x, int y, int size, String id)
    {
        this.rX = x;
        this.rY = y;
        this.size = size;

        this.x = rX * this.size;
        this.y = rY * this.size;

        this.id = id;
    }

    public void addNode(Node n)
    {
        nodes.add(n);

        n.belongsTo(this);
    }

    public void removeNode(Node n)
    {
        nodes.remove(n);
        n.belongsTo(null);
    }

    public void draw(Graphics g, int x, int y, int w, int h)
    {
        this.dx = x;
        this.dy = y;
        this.dw = w;
        this.dh = h;

        g.drawRect(x, y, w, h);
    }

    public boolean isThisChunk(Chunk c)
    {
        return this.id == c.id;
    }

    public boolean isInsideChunk(Node n)
    {
        boolean left = n.getX() >= this.x;
        boolean upper = n.getY() >= this.y;

        boolean right = n.getX() < this.x + this.size;
        boolean lower = n.getY() < this.y + this.size;

        return left && right && upper && lower;
    }

    public int getRelativeX() {
        return rX;
    }
    public int getRelativeX(int off) {
        return getRelativeX() + off;
    }


    public int getRelativeY() {
        return rY;
    }
    public int getRelativeY(int off) {
        return getRelativeY() + off;
    }

    public int getSize() {
        return size;
    }

    public List<Node> getNodes() {
        return (List<Node>) nodes.clone();
    }

    public void setVisible(boolean b)
    {
        visible = b;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getDrawX() {
        return dx;
    }

    public int getDrawY() {
        return dy;
    }

    public String getID() {
        return id;
    }
    public boolean isId(String s)
    {
        return s.equals(id);
    }
}
