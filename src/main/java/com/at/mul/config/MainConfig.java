package com.at.mul.config;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

@Configuration
@ComponentScan
@EnableTransactionManagement
public class MainConfig {

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setDatabase(Database.H2);
		return hibernateJpaVendorAdapter;
	}

	@Bean
	public UserTransaction userTransaction() throws Throwable {
		UserTransactionImp userTransactionImp = new UserTransactionImp();
		userTransactionImp.setTransactionTimeout(10000);
		return userTransactionImp;
	}

	@Bean
	public TransactionManager atomikosTransactionManager() throws Throwable {
		UserTransactionManager userTransactionManager = new UserTransactionManager();
		userTransactionManager.setForceShutdown(false);

		AtomikosJtaPlatform.transactionManager = userTransactionManager;

		return userTransactionManager;
	}

	@Bean
	@DependsOn({ "userTransaction", "atomikosTransactionManager" })
	public PlatformTransactionManager transactionManager() throws Throwable {
		UserTransaction userTransaction = userTransaction();

		AtomikosJtaPlatform.transaction = userTransaction;

		TransactionManager atomikosTransactionManager = atomikosTransactionManager();
		return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
	}

}
