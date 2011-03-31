package org.doxla.graphflow.service;

import org.doxla.graphflow.domain.workflow.WorkflowGraph;
import org.doxla.graphflow.domain.workflow.WorkflowTransition;
import org.doxla.graphflow.domain.workflow.WorkflowStep;

import java.util.List;
import java.util.UUID;

public interface WorkflowGraphService {
    List<WorkflowStep> listSteps(WorkflowGraph graph);
    List<WorkflowTransition> listTransitions(WorkflowGraph graph);
    void addTransition(WorkflowGraph workflowGraph, WorkflowTransition transition);

    List<UUID> listGraphs();

    WorkflowGraph findById(UUID uuid);

    UUID create();
}
