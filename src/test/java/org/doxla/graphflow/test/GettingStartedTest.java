package org.doxla.graphflow.test;

import org.doxla.graphflow.domain.AbstractGraphDatabaseTestCase;
import org.doxla.graphflow.domain.graph.GraphBuilder;
import org.junit.Test;
import org.neo4j.graphdb.Node;

public class GettingStartedTest extends AbstractGraphDatabaseTestCase {

    @Test
    public void createGraph() throws Exception {

        GraphBuilder build = new GraphBuilder(db);
        build.node("start");
        build.node("validate");
        build.node("beginProvisioning");
        build.node("rejected");
        build.node("sentToBt");
        build.node("accepted");
        build.node("confirmed");
        build.node("btComplete");
        build.node("complete");

        build.transition("start", "validate", "beginProvisioning", "rejected", "sentToBt",
                         "accepted", "confirmed", "btComplete", "complete");

        build.transition("validate", "rejected");
        build.startsIn("start");

        Node workflow = build.build();
    }
}