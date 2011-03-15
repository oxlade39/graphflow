package org.doxla.graphflow.controller;

import org.doxla.graphflow.domain.workflow.WorkflowGraph;
import org.doxla.graphflow.service.WorkflowGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static org.doxla.graphflow.domain.workflow.WorkflowTransitionBuilder.transition;

@Controller
@RequestMapping(value = "/flow/{flowId}")
public class GraphController {
    @Autowired
    private WorkflowGraphService workflowGraphService;

    @Autowired
    private WorkflowGraph workflowGraph;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView flow(@PathVariable String flowId) {
        ModelAndView graph = new ModelAndView("graph");
        graph.addObject("id", flowId);
        graph.addObject("steps", workflowGraphService.listSteps(workflowGraph));
        graph.addObject("transitions", workflowGraphService.listTransitions(workflowGraph));
        return graph;
    }

    @RequestMapping(value = "/transition", method = RequestMethod.POST)
    public String createTransition(@RequestParam String to, @RequestParam String from, @PathVariable String flowId) {
        workflowGraphService.addTransition(workflowGraph, transition().to(to).from(from).build());
        return "redirect:/flow/" + flowId;
    }

}
