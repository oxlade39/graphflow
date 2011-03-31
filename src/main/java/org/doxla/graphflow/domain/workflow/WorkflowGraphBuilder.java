package org.doxla.graphflow.domain.workflow;

import org.doxla.graphflow.domain.graph.GraphBuilder;
import org.doxla.graphflow.domain.graph.NodeCache;
import org.doxla.graphflow.domain.graph.RelationshipBuilder;
import org.neo4j.graphdb.GraphDatabaseService;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class WorkflowGraphBuilder {
    private final GraphBuilder graphBuilder;
    private final RelationshipBuilder relationshipBuilder;

    public WorkflowGraphBuilder(GraphDatabaseService graphDatabaseService) {
        NodeCache nodeCache = new NodeCache();
        UUID graphId = randomUUID();
        this.graphBuilder = new GraphBuilder(graphDatabaseService, nodeCache, graphId);
        this.relationshipBuilder = new RelationshipBuilder(graphDatabaseService, nodeCache, graphId);
    }

    public WorkflowGraphBuilder with(WorkflowStep step) {
        graphBuilder.node(step.name());
        return this;
    }

    public WorkflowGraphBuilder start(WorkflowStep start) {
        with(start);
        graphBuilder.startsIn(start.name());
        return this;
    }

    public WorkflowGraphBuilder with(WorkflowTransitionBuilder transitionBuilder) {
        WorkflowTransition transition = transitionBuilder.build();
        relationshipBuilder.transition(transition.from(), transition.to());
        return this;
    }

    public WorkflowGraph build() {
        return new WorkflowGraph(graphBuilder.build());
    }
}
