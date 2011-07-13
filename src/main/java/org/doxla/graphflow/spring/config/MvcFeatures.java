package org.doxla.graphflow.spring.config;

import org.doxla.graphflow.controller.TransitionParameterMapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(
        basePackages = "org.doxla.graphflow.controller",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)}
)
public class MvcFeatures extends WebMvcConfigurerAdapter {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new TransitionParameterMapper());
    }

    @Override
    public void configureResourceHandling(ResourceConfigurer configurer) {
        configurer.addPathMapping("/resources/**");
        configurer.addResourceLocation("META-INF/resources/");
    }


}
