package org.doxla.graphflow.spring.config;

import org.springframework.context.annotation.Feature;
import org.springframework.context.annotation.FeatureConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.config.TxAnnotationDriven;

@FeatureConfiguration
public class TransactionFeatures {

	@Feature
	public TxAnnotationDriven txAnnotationDriven(PlatformTransactionManager txManager) {
		return new TxAnnotationDriven(txManager);
	}

}
