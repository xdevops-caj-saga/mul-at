package com.at.mul.config;

import com.at.mul.repository.customer.CustomerDatasourceProperties;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(basePackages = "com.at.mul.repository.customer", entityManagerFactoryRef = "customerEntityManager", transactionManagerRef = "transactionManager")
@EnableConfigurationProperties(CustomerDatasourceProperties.class)
public class CustomerConfig {

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private CustomerDatasourceProperties customerDatasourceProperties;

    @Bean
    public DataSource customerDataSource() {
        JdbcDataSource h2XaDataSource = new JdbcDataSource();
        h2XaDataSource.setURL(customerDatasourceProperties.getUrl());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(h2XaDataSource);
        xaDataSource.setUniqueResourceName("xads1");
        return xaDataSource;
    }

    @Bean
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean customerEntityManager() throws Throwable {

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(customerDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("com.at.mul.domain.customer");
        entityManager.setPersistenceUnitName("customerPersistenceUnit");
        entityManager.setJpaPropertyMap(properties);
        return entityManager;
    }

}
