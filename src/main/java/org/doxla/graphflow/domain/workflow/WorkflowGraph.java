package org.doxla.graphflow.domain.workflow;

import org.doxla.graphflow.domain.graph.index.MyIndices;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.doxla.graphflow.domain.graph.type.NodeTypes.POSITION_POINTER;
import static org.doxla.graphflow.domain.graph.type.RelationshipTypes.CURRENT_POSITION;
import static org.doxla.graphflow.domain.graph.type.RelationshipTypes.TRANSITIONS_TO;
import static org.doxla.graphflow.domain.graph.type.NodeProperties.NODE_ID;
import static org.doxla.graphflow.domain.graph.type.NodeProperties.NODE_NAME;
import static org.doxla.graphflow.domain.graph.type.NodeProperties.NODE_TYPE;

public class WorkflowGraph {

    private final Node startNode;

    public WorkflowGraph(Node startNode) {
        this.startNode = startNode;
    }

    public UUID getId() {
        return UUID.fromString((String) startNode.getProperty(NODE_ID));
    }

    public WorkflowStep getStartStep() {
        return nodeToStep(startNode);
    }

    public WorkflowStep getCurrentPosition() {
        return nodeToStep(currentPositionNode());
    }

    public List<WorkflowTransition> getAvailableTransitions() {
        List<WorkflowTransition> transitions = new ArrayList<WorkflowTransition>();
        Traverser traverse = currentPositionNode().traverse(Traverser.Order.BREADTH_FIRST, StopEvaluator.DEPTH_ONE,
                ReturnableEvaluator.ALL_BUT_START_NODE, TRANSITIONS_TO, Direction.OUTGOING);
        for (Node node : traverse) {
            transitions.add(nodeToTransition(node));
        }
        for (WorkflowTransition transition : transitions) {
            System.out.println("got transition = " + transition);
        }
        return transitions;
    }

    public static WorkflowStep nodeToStep(Node startNode1) {
        return WorkflowStep.step((String) startNode1.getProperty(NODE_NAME));
    }

    private Node currentPositionNode() {
        IndexHits<Node> indexHits = index().query(String.format("%s:%s AND %s:%s",
                                                    NODE_ID, getId().toString(),
                                                    NODE_TYPE, POSITION_POINTER));
        Node positionPointer = indexHits.getSingle();
        Relationship currentPositionRelationship = positionPointer.getSingleRelationship(CURRENT_POSITION, Direction.OUTGOING);
        return currentPositionRelationship.getEndNode();
    }

    private WorkflowTransition nodeToTransition(Node node) {
        return new WorkflowTransition(getCurrentPosition().name(), nodeToStep(node).name());
    }

    private Index<Node> index() {
        return startNode.getGraphDatabase().index().forNodes(MyIndices.TEXT_INDEX.name());
    }

    Node getUnderlyingNode() {
        return startNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkflowGraph that = (WorkflowGraph) o;

        return that.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
