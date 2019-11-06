package Controller.Routines;

import Model.Force.IForce;
import Model.Graph.ForceNode;
import Model.Graph.Graph;
import Model.Graph.Relationship;
import View.GraphViewController;

public class RelationshipRoutine implements IForceRoutine {

    GraphViewController viewController;
    IForce force;

    public RelationshipRoutine(GraphViewController vCon, IForce f)
    {
        viewController = vCon;
        force = f;
    }

    @Override
    public void doRoutine() {
        for (Relationship r: viewController.getLoadedRelationships())
        {
            force.applyForceForBoth((ForceNode) r.getN1(), (ForceNode) r.getN2());
        }
    }
}
