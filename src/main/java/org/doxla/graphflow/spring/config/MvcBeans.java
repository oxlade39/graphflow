package org.doxla.graphflow.spring.config;

import freemarker.template.utility.XmlEscape;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import java.util.*;

@Configuration
public class MvcBeans {

    @Bean
    public ConversionService conversionService() {
        DefaultFormattingConversionService bean = new DefaultFormattingConversionService();
        return bean;
    }


    @Bean
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver() {
        ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setMediaTypes(mediaTypes());
        viewResolver.setViewResolvers(viewResolvers());
        viewResolver.setDefaultViews(defaultViews());
        return viewResolver;
    }

    private Map<String, String> mediaTypes() {
        return new HashMap<String, String>() {{
            put("html", "text/html");
            put("json", "application/json");
        }};
    }

    private List<ViewResolver> viewResolvers() {
        return new ArrayList<ViewResolver>() {{
            add(freemarkerViewResolver());
            add(beanNameViewResolver());
        }};
    }

    private List<View> defaultViews() {
        return Arrays.<View>asList(mappingJacksonJSONView());
    }

    @Bean
    public MappingJacksonJsonView mappingJacksonJSONView() {
        MappingJacksonJsonView mappingJacksonJsonView = new MappingJacksonJsonView();
        return mappingJacksonJsonView;
    }

    @Bean
    public BeanNameViewResolver beanNameViewResolver() {
        return new BeanNameViewResolver();
    }

    /**
     * <bean id="content" class="com.springsource.samples.rest.SampleContentAtomView"/>
     */


    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/freemarker/");
        HashMap<String, Object> freemarkerVariables = freemarkerVariables();
        freeMarkerConfigurer.setFreemarkerVariables(freemarkerVariables);
        return freeMarkerConfigurer;
    }

    private HashMap<String, Object> freemarkerVariables() {
        HashMap<String, Object> freemarkerVariables = new HashMap<String, Object>();
        freemarkerVariables.put("xml_escape", new XmlEscape());
        return freemarkerVariables;
    }
}
