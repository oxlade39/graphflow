package org.doxla.graphflow.domain.workflow;

import org.doxla.graphflow.domain.graph.GraphBuilder;
import org.neo4j.graphdb.GraphDatabaseService;

public class WorkflowGraphBuilder {
    private final GraphBuilder graphBuilder;

    public WorkflowGraphBuilder(GraphDatabaseService graphDatabaseService) {
        this.graphBuilder = new GraphBuilder(graphDatabaseService);
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
        graphBuilder.transition(transition.from(), transition.to());
        return this;
    }

    public WorkflowGraph build() {
        return new WorkflowGraph(graphBuilder.build());
    }
}
