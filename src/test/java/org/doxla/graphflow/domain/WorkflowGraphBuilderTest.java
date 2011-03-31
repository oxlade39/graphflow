package org.doxla.graphflow.domain;

import org.doxla.graphflow.domain.graph.index.MyIndices;
import org.doxla.graphflow.domain.graph.type.NodeProperties;
import org.doxla.graphflow.domain.workflow.WorkflowGraph;
import org.doxla.graphflow.domain.workflow.WorkflowGraphBuilder;
import org.doxla.graphflow.domain.workflow.WorkflowTransition;
import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.doxla.graphflow.domain.workflow.WorkflowStep.step;
import static org.doxla.graphflow.domain.workflow.WorkflowTransitionBuilder.transition;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class WorkflowGraphBuilderTest extends AbstractGraphDatabaseTestCase {

    @Test
    public void createsWorkflowGraph() {
        WorkflowGraphBuilder graphBuilder = new WorkflowGraphBuilder(db);
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

        WorkflowGraph workflowGraph = graphBuilder.build();

        assertThat(workflowGraph.getStartStep(), is(CoreMatchers.equalTo(workflowGraph.getCurrentPosition())));
        List<WorkflowTransition> transitions = workflowGraph.getAvailableTransitions();
        assertThat(transitions.size(), is(CoreMatchers.equalTo(1)));
        assertThat(transitions, hasItem(new WorkflowTransition("start", "validate")));
    }

    @Test
    public void canFindFromLoads() throws Exception {
        Index<Node> index = db.index().forNodes(MyIndices.TEXT_INDEX.name());
        IndexHits<Node> indexHits = index.query("RELATIONSHIP_ID: 123*");
        for (Node indexHit : indexHits) {
            if(indexHit.hasProperty(NodeProperties.NODE_NAME)){
                System.out.printf("\nindexHit = name = %s, id = %s",
                        indexHit.getProperty(NodeProperties.NODE_NAME),
                        indexHit.getProperty(NodeProperties.NODE_ID));
            }
        }
    }

    @Ignore("takes ages")
    @Test
    public void canCreateMillions() throws Exception {
        endTransaction();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 1000; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {

                    new TransactionTemplate() {

                        @Override
                        protected void doExecute() {
                            createsWorkflowGraph();
                        }
                    }.execute();
                    }
                }
            });
        }
        executorService.awaitTermination(1, TimeUnit.DAYS);
    }

}
