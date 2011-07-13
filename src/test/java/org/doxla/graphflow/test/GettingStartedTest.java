package org.doxla.graphflow.test;

import org.doxla.graphflow.domain.AbstractGraphDatabaseTestCase;
import org.doxla.graphflow.domain.graph.GraphBuilder;
import org.doxla.graphflow.domain.graph.NodeCache;
import org.doxla.graphflow.domain.graph.RelationshipBuilder;
import org.junit.Test;
import org.neo4j.graphdb.Node;

import java.util.UUID;

public class GettingStartedTest extends AbstractGraphDatabaseTestCase {

    @Test
    public void createGraph() throws Exception {

        NodeCache nodeCache = new NodeCache();
        UUID id = UUID.randomUUID();
        GraphBuilder graphBuilder = new GraphBuilder(db, nodeCache, id);
        RelationshipBuilder relationshipBuilder = new RelationshipBuilder(db, nodeCache, id);
        graphBuilder
                .node("start")
                .node("validate")
                .node("beginProvisioning")
                .node("rejected")
                .node("sentToBt")
                .node("accepted")
                .node("confirmed")
                .node("btComplete")
                .node("complete");

        relationshipBuilder.transition(
                "start", "validate",
                "beginProvisioning",
                "rejected", "sentToBt",
                "accepted", "confirmed",
                "btComplete", "complete");

        relationshipBuilder.transition("validate", "rejected");
        graphBuilder.startsIn("start");

        Node workflow = graphBuilder.build();

    }
}