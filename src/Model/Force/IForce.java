package Model.Force;

import Model.Graph.ForceNode;

public interface IForce {

    void applyForceForFirst(ForceNode n1, ForceNode n2);
    void applyForceForBoth(ForceNode n1, ForceNode n2);

}
