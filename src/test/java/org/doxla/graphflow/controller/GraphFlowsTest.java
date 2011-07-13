package org.doxla.graphflow.controller;

import org.junit.Test;

import java.util.UUID;

import static junit.framework.Assert.assertEquals;

public class GraphFlowsTest {

    private GraphFlows underTest = new GraphFlows();
    private UUID nextId;

    @Test
    public void creatingANewGraphRedirectsToLocationOfNewFlow() throws Exception {
        nextId = UUID.randomUUID();
        underTest.workflowGraphService = new WorkflowGraphServiceStubBuilder().withNextId(nextId).build();

        String graphResourceLocation = underTest.create();
        assertEquals("redirect:/flow/" + nextId, graphResourceLocation);
    }

}
