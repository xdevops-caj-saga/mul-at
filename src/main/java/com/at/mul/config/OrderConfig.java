package com.at.mul.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.at.mul.repository.order.OrderDatasourceProperties;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(basePackages = "com.at.mul.repository.order", entityManagerFactoryRef = "orderEntityManager", transactionManagerRef = "transactionManager")
@EnableConfigurationProperties(OrderDatasourceProperties.class)
public class OrderConfig {

	@Autowired
	private JpaVendorAdapter jpaVendorAdapter;

	@Autowired
	private OrderDatasourceProperties orderDatasourceProperties;

	@Bean
	public DataSource orderDataSource() {
		JdbcDataSource h2XaDataSource = new JdbcDataSource();
		h2XaDataSource.setURL(orderDatasourceProperties.getUrl());

		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		xaDataSource.setXaDataSource(h2XaDataSource);
		xaDataSource.setUniqueResourceName("xads2");
		return xaDataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean orderEntityManager() throws Throwable {

		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
		properties.put("javax.persistence.transactionType", "JTA");

		LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
		entityManager.setJtaDataSource(orderDataSource());
		entityManager.setJpaVendorAdapter(jpaVendorAdapter);
		entityManager.setPackagesToScan("com.at.mul.domain.order");
		entityManager.setPersistenceUnitName("orderPersistenceUnit");
		entityManager.setJpaPropertyMap(properties);
		return entityManager;
	}

}
