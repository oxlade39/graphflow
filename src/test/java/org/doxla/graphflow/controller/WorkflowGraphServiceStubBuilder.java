package org.doxla.graphflow.controller;

import org.doxla.graphflow.domain.workflow.WorkflowGraph;
import org.doxla.graphflow.domain.workflow.WorkflowStep;
import org.doxla.graphflow.domain.workflow.WorkflowTransition;
import org.doxla.graphflow.service.WorkflowGraphService;

import java.util.List;
import java.util.UUID;

public class WorkflowGraphServiceStubBuilder {
    private UUID nextId;

    public WorkflowGraphServiceStubBuilder() {
    }

    public WorkflowGraphServiceStubBuilder withNextId(UUID nextId) {
        this.nextId = nextId;
        return this;
    }

    public WorkflowGraphService build() {
        return new WorkflowGraphService() {
            @Override
            public List<WorkflowStep> listSteps(WorkflowGraph graph) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<WorkflowTransition> listTransitions(WorkflowGraph graph) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void addTransition(WorkflowGraph workflowGraph, WorkflowTransition transition) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<UUID> listGraphs() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public WorkflowGraph findById(UUID uuid) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public UUID create() {
                return nextId;
            }
        };

    }
}
