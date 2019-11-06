package Program;

import Controller.ForceController;
import Controller.Routines.RelationshipRoutine;
import Model.Force.SpringForce;
import Model.Graph.*;
import View.GraphView;
import View.GraphView02;
import View.GraphViewController;

import javax.swing.*;

public class MainApplication {

    public static void main(String[] args) throws InterruptedException {
        Graph graph = new Graph(5, 5, 8);

        Node n1 = new ForceNode(1, 1);
        Node n2 = new ForceNode(12, 12);
        Node n3 = new ForceNode(1, 12);

        graph.putNode(n1);
        graph.putNode(n2);
        graph.putNode(n3);

        graph.putRelationship(new Relationship(n1, n2));
        graph.putRelationship(new Relationship(n1, n3));
        graph.putRelationship(new Relationship(n2, n3));

//        for (int i=0;i<10;i++)
//        {
//            n.move(1, 0);
//            graph.updateNode(n);
//        }

        JFrame frame = new JFrame("Main");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphViewController viewController = new GraphViewController(graph);
        GraphView02 view = new GraphView02(viewController);

        frame.add(view);
        frame.pack();

        frame.addMouseListener(view);
        frame.addMouseMotionListener(view);
        frame.addKeyListener(view);

        frame.setVisible(true);

        ForceController fCon = new ForceController(viewController, graph);
        fCon.addRoutine(new RelationshipRoutine(viewController, new SpringForce(5)));

        while (true) {
            viewController.update();

            fCon.doRoutines();
            fCon.finish();

            view.update();

            Thread.sleep(100);
        }
    }
}
