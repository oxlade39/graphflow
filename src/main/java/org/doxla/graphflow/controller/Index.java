package org.doxla.graphflow.controller;

import org.doxla.graphflow.service.WorkflowGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/")
public class Index {

    @Autowired
    private WorkflowGraphService workflowGraphService;

    @RequestMapping(method = GET)
    public ModelAndView get(){
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("graphs", workflowGraphService.listGraphs());
        return modelAndView;
    }
}
