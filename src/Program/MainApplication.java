package Program;

import Controller.ForceController;
import Controller.Routines.ChunkRadiusRoutine;
import Controller.Routines.RelationshipRoutine;
import Model.Force.RepelForce;
import Model.Force.SpringForce;
import Model.Graph.*;
import View.GraphView02;
import View.GraphViewController;

import javax.swing.*;

public class MainApplication {

    public static void main(String[] args) throws InterruptedException {
        Graph graph = new Graph(5, 5, 8);

//        Node n1 = new ForceNode(1, 1);
//        Node n2 = new ForceNode(2, 2);
//        Node n3 = new ForceNode(1, 12);
//
//        graph.putNode(n1);
//        graph.putNode(n2);
//        graph.putNode(n3);
//
//        graph.putRelationship(new Relationship(n1, n2));
//        graph.putRelationship(new Relationship(n1, n3));
//        graph.putRelationship(new Relationship(n2, n3));

        int w = 10, h =10;
        Node[][] grid = new Node[w][h];
        for (int i=0;i<w;i++)
        {
            for (int ii=0;ii<h;ii++)
            {
                grid[i][ii] = new ForceNode(i, ii);
                graph.putNode(grid[i][ii]);

                if (i>0)
                {
                    graph.putRelationship(new Relationship(grid[i][ii], grid[i-1][ii]));
                }
                if (ii>0)
                {
                    graph.putRelationship(new Relationship(grid[i][ii], grid[i][ii-1]));
                }
            }
        }

        JFrame frame = new JFrame("Main");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphViewController viewController = new GraphViewController(graph);
        GraphView02 view = new GraphView02(viewController);

        frame.add(view);
        frame.pack();

        view.addMouseListener(view);
        view.addMouseMotionListener(view);
        frame.addKeyListener(view);

        frame.setVisible(true);

        ForceController fCon = new ForceController(viewController, graph);
        fCon.addRoutine(new RelationshipRoutine(viewController, new SpringForce(1, 0.1)));
        fCon.addRoutine(new ChunkRadiusRoutine(viewController, graph, new RepelForce(8, 0.1), 3));

        while (true) {
            viewController.update();

            fCon.doRoutines();
            fCon.finish();

            view.update();
        }
    }
}
