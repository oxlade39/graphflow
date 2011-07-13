package org.doxla.graphflow.spring.config;

import org.apache.log4j.Logger;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class MyWebAppInitializer implements WebApplicationInitializer {
    private final Logger LOG = Logger.getLogger(getClass());

    public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        root.scan("org.doxla.graphflow.spring.config");
        resolveEnvironment(root);

        servletContext.addListener(new ContextLoaderListener(root));

        ServletRegistration.Dynamic appServlet =
                servletContext.addServlet("graphflow", new DispatcherServlet(new GenericWebApplicationContext()));
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");
    }

    private void resolveEnvironment(AnnotationConfigWebApplicationContext root) {
        CloudEnvironment env = new CloudEnvironment();
        if (env.getInstanceInfo() != null) {
            LOG.info("Detected cloud environment");
            LOG.info("Cloud API: " + env.getCloudApiUri());
            LOG.info("Cloud InstanceInfo Name: " + env.getInstanceInfo().getName());
            LOG.info("Cloud InstanceInfo Host: " + env.getInstanceInfo().getHost());
            LOG.info("Cloud InstanceInfo URIs: " + env.getInstanceInfo().getUris());

            root.getEnvironment().setActiveProfiles("cloud");
        } else {
            LOG.info("Detected non cloud environment setting default profile");
            root.getEnvironment().setActiveProfiles("default");
        }
    }
}
