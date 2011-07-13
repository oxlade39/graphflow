package org.doxla.graphflow.controller;

import org.apache.log4j.Logger;
import org.doxla.graphflow.domain.workflow.WorkflowTransition;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.doxla.graphflow.domain.workflow.WorkflowTransitionBuilder.transition;

public class TransitionParameterMapper implements HandlerMethodArgumentResolver {

    private Logger log = Logger.getLogger(getClass());

    public boolean supportsParameter(MethodParameter parameter) {
        return WorkflowTransition.class.equals(parameter.getParameterType());
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String to = (String) webRequest.getAttribute("to", WebRequest.SCOPE_REQUEST);
        String from = (String) webRequest.getAttribute("from", WebRequest.SCOPE_REQUEST);
        return transition().to(to).from(from).build();
    }

}
