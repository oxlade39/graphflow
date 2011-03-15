package org.doxla.graphflow.domain.workflow;

public class WorkflowTransitionBuilder {
    private String from;
    private String to;

    public static WorkflowTransitionBuilder transition() {
        return new WorkflowTransitionBuilder();
    }

    public WorkflowTransitionBuilder from(String from) {
        this.from = from;
        return this;
    }

    public WorkflowTransitionBuilder to(String to) {
        this.to = to;
        return this;
    }

    public WorkflowTransition build() {
        if(from == null) throw new IllegalStateException("Needs a from");
        if(to == null) throw new IllegalStateException("Needs a to");
        return new WorkflowTransition(from, to);
    }
}
