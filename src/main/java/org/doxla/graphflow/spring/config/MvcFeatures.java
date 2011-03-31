package org.doxla.graphflow.spring.config;

import org.springframework.context.annotation.ComponentScanSpec;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Feature;
import org.springframework.context.annotation.FeatureConfiguration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.servlet.config.MvcAnnotationDriven;
import org.springframework.web.servlet.config.MvcResources;
import org.springframework.web.servlet.config.MvcViewControllers;

@FeatureConfiguration
public class MvcFeatures {

    @Feature
    public MvcAnnotationDriven annotationDriven(ConversionService conversionService) {
        return new MvcAnnotationDriven().conversionService(conversionService);

    }

    @Feature
    public MvcResources resources() {
        return new MvcResources("/resources/**", "META-INF/resources/");
    }

    @Feature
    public ComponentScanSpec componentScan() {
        return new ComponentScanSpec("org.doxla.graphflow.controller").excludeFilters(
                new AnnotationTypeFilter(Configuration.class),
                new AnnotationTypeFilter(FeatureConfiguration.class));
    }

}
