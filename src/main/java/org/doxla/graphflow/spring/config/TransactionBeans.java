package org.doxla.graphflow.spring.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.impl.transaction.SpringTransactionManager;
import org.neo4j.kernel.impl.transaction.UserTransactionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;

@Configuration
public class TransactionBeans {

    @Autowired
    private GraphDatabaseService graphDatabaseService;

    @Bean
    public SpringTransactionManager neo4jTransactionManagerService() {
        return new SpringTransactionManager(graphDatabaseService);
    }

    @Bean
    public JtaTransactionManager neo4jTransactionManager(GraphDatabaseService graphDatabaseService) {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(neo4jTransactionManagerService());
        jtaTransactionManager.setUserTransaction(neo4jUserTransactionManager());
        return jtaTransactionManager;
    }

    @Bean
    public UserTransaction neo4jUserTransactionManager() {
        return new UserTransactionImpl(graphDatabaseService);
    }
}
