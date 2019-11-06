package Controller;

import Controller.Routines.IForceRoutine;
import Model.Chunk;
import Model.Graph.ForceNode;
import Model.Graph.Graph;
import Model.Graph.Node;
import View.GraphViewController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForceController {

    List<IForceRoutine> routines = new ArrayList<>();

    GraphViewController viewController;
    Graph model;

    public ForceController(GraphViewController vCon, Graph m)
    {
        viewController = vCon;
        model = m;
    }

    public void addRoutine(IForceRoutine r)
    {
        routines.add(r);
    }

    public void doRoutines()
    {
        for (IForceRoutine r: routines)
        {
            r.doRoutine();
        }
    }

    public void finish()
    {
        for (Chunk c: viewController.getLoadedChunks())
        {
            for (Node _n: c.getNodes())
            {
                ForceNode n = (ForceNode)_n;
                double[] vector = n.findResultantForce();

//                System.out.println(Arrays.toString(vector));

                n.move(vector[0], vector[1]);
                model.updateNode(n);
            }
        }
    }
}
