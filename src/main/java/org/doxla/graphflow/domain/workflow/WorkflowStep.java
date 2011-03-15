package org.doxla.graphflow.domain.workflow;

public class WorkflowStep {
    private final String name;

    WorkflowStep(String name) {
        this.name = name;
    }

    public static WorkflowStep step(String name) {
        return new WorkflowStep(name);
    }

    public String name() {
        return name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkflowStep that = (WorkflowStep) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
