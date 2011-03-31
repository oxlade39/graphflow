package org.doxla.graphflow.controller;

import org.doxla.graphflow.domain.workflow.WorkflowGraph;
import org.doxla.graphflow.domain.workflow.WorkflowStep;
import org.doxla.graphflow.domain.workflow.WorkflowTransition;
import org.doxla.graphflow.service.WorkflowGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

import static org.doxla.graphflow.controller.ViewUtils.redirect;
import static org.doxla.graphflow.domain.workflow.WorkflowTransitionBuilder.transition;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/flow")
public class GraphFlows {
    @Autowired
    WorkflowGraphService workflowGraphService;

    @RequestMapping(value = "/{flowId}", method = GET)
    public ModelAndView flow(@PathVariable String flowId) {
        ModelAndView graph = new ModelAndView("graph");
        WorkflowGraph workflowGraph = graphFor(flowId);
        graph.addObject("id", flowId);
        graph.addObject("graph", workflowGraph);
        graph.addObject("steps", stepsFor(workflowGraph));
        graph.addObject("transitions", transitionsFor(workflowGraph));
        return graph;
    }

    @RequestMapping(value = "/{flowId}/transition", method = POST)
    public String createTransition(@RequestParam String to, @RequestParam String from, @PathVariable String flowId) {
        workflowGraphService.addTransition(graphFor(flowId), transition().to(to).from(from).build());
        return redirect("/flow/%s", flowId);
    }

    @RequestMapping(method = POST)
    public String create() {
        UUID id = workflowGraphService.create();
        return redirect("/flow/%s", id);
    }

    private List<WorkflowTransition> transitionsFor(WorkflowGraph workflowGraph) {
        return workflowGraphService.listTransitions(workflowGraph);
    }

    private List<WorkflowStep> stepsFor(WorkflowGraph workflowGraph) {
        return workflowGraphService.listSteps(workflowGraph);
    }

    private WorkflowGraph graphFor(String flowId) {
        return workflowGraphService.findById(UUID.fromString(flowId));
    }

}
