package org.doxla.graphflow.domain.workflow;

public class WorkflowTransition {

    private final String from;
    private final String to;

    public WorkflowTransition(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String from() {
        return this.from;
    }

    public String to() {
        return this.to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkflowTransition that = (WorkflowTransition) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}
