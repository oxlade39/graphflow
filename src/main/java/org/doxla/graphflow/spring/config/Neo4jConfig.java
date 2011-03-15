package org.doxla.graphflow.spring.config;

import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jConfig {

    @Bean(destroyMethod = "shutdown")
    public EmbeddedGraphDatabase db() {
        return new EmbeddedGraphDatabase("var/example3");
    }
}
