package Program;

import Model.Graph.Graph;
import Model.Graph.Node;
import Model.Graph.Relationship;
import View.GraphView;
import View.GraphView02;
import View.GraphViewController;

import javax.swing.*;

public class MainApplication {

    public static void main(String[] args)
    {
        Graph graph = new Graph(5, 5, 8);

        Node n1 = new Node(1, 1);
        Node n2 = new Node(12, 12);

        graph.putNode(n1);
        graph.putNode(n2);

        graph.putRelationship(new Relationship(n1, n2));

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

        view.update();
    }
}
