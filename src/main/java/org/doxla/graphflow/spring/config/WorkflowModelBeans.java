package org.doxla.graphflow.spring.config;

import org.doxla.graphflow.domain.workflow.WorkflowGraph;
import org.doxla.graphflow.service.Neo4JWorkflowGraphService;
import org.doxla.graphflow.service.WorkflowGraphService;
import org.doxla.graphflow.domain.workflow.WorkflowGraphBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import static org.doxla.graphflow.domain.workflow.WorkflowStep.step;
import static org.doxla.graphflow.domain.workflow.WorkflowTransitionBuilder.transition;

@Configuration
public class WorkflowModelBeans {

    @Autowired
    private GraphDatabaseService graphDatabaseService;

    @Bean
    public WorkflowGraphBuilder workflowGraphBuilder() {
        return new WorkflowGraphBuilder(graphDatabaseService);
    }

    @Bean @Transactional
    public WorkflowGraph exampleGraph() {
        WorkflowGraphBuilder graphBuilder = workflowGraphBuilder();
        graphBuilder.start(step("start"));
        graphBuilder.with(step("validate"));
        graphBuilder.with(step("sentToBT"));
        graphBuilder.with(step("confirmed"));
        graphBuilder.with(step("complete"));
        graphBuilder.with(step("rejected"));
        graphBuilder.with(step("cancelled"));

        graphBuilder.with(transition().from("start").to("validate"));
        graphBuilder.with(transition().from("validate").to("sentToBT"));
        graphBuilder.with(transition().from("sentToBT").to("confirmed"));
        graphBuilder.with(transition().from("confirmed").to("complete"));

        graphBuilder.with(transition().from("validate").to("rejected"));
        graphBuilder.with(transition().from("sentToBT").to("rejected"));
        graphBuilder.with(transition().from("confirmed").to("rejected"));

        graphBuilder.with(transition().from("validate").to("cancelled"));
        graphBuilder.with(transition().from("sentToBT").to("cancelled"));
        graphBuilder.with(transition().from("confirmed").to("cancelled"));

        return graphBuilder.build();
    }

    @Bean
    public WorkflowGraphService workflowGraphService() {
        return new Neo4JWorkflowGraphService(graphDatabaseService);
    }
}
