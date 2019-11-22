package Model;

import Model.Graph.Node;
import Model.Graph.ViewNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Chunk extends ViewNode{

    private String id;

    ArrayList<Node> nodes = new ArrayList<>();

    int size;

    int dw, dh;

    boolean visible;

    public Chunk(int x, int y, int size, String id)
    {
        super(x, y);

        this.size = size;
        this.id = id;

        updateRelative();
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

    //draw the under lying node
    @Override
    public void draw(Graphics g, int x, int y) {
        this.dx = x;
        this.dy = y;

        g.drawOval(dx - this.rad, dy - this.rad, this.diam, this.diam);
    }

    //draw the chunk
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
        boolean left = n.getX() >= this.rX;
        boolean upper = n.getY() >= this.rY;

        boolean right = n.getX() < this.rX + this.size;
        boolean lower = n.getY() < this.rY + this.size;

        return left && right && upper && lower;
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

    public String getID() {
        return id;
    }
    public boolean isId(String s)
    {
        return s.equals(id);
    }

    @Override
    protected void updateRelative() {
        //slightly different relative calculation for chunks
        this.rX = (int)this.x * this.size;
        this.rY = (int)this.y * this.size;
    }

    @Override
    public void move(double x, double y) {
        //chunks dont move
    }

    @Override
    public Chunk getBelongsTo() {
        //doesn't belong to any other node, ie itself
        return this;
    }
}
