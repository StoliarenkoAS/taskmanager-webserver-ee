package ru.stoliarenkoas.tm.webserver.configuration;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.stoliarenkoas.tm.webserver.util.JwtTokenProvider;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:application.properties")
@EnableJpaRepositories(value = "ru.stoliarenkoas.tm.webserver.repository")
public class JpaConfiguration {


    @Bean(name = "dataSource")
    public DataSource getDataSource(
            @Value("${connector.class}") final String driverClass,
            @Value("${database.url}") final String databaseUrl,
            @Value("${database.login}") final String databaseLogin,
            @Value("${database.password}") final String databasePassword
    ) {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseLogin);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(
            final DataSource dataSource,
            @Value("${hibernate.show_sql}") final String showSql,
            @Value("${hibernate.hbm2dll.auto}") final String tableStrategy,
            @Value("${hibernate.dialect}") final String dialect,
            @Value("${hibernate.cache.use_second_level_cache}") final String useCache,
            @Value("${hibernate.cache.use_query_cache}") final String useQueryCache,
            @Value("${hibernate.cache.region_prefix}") final String regionPrefix,
            @Value("${hibernate.cache.provider_configuration_file_resource_path}") final String configFile,
            @Value("${hibernate.cache.region.factory_class}")
            final String cacheFactoryClass,
            @Value("${hibernate.implicit_naming_strategy}")
            final String implicitNamingStrategy,
            @Value("${hibernate.physical_naming_strategy}")
            final String physicalNamingStrategy,
            @Value("${hibernate.cache.use_minimal_puts}") final String useMinPuts,
            @Value("${hibernate.cache.hazelcast.use_lite_member}") final String useLiteMember
    ) {
        final LocalContainerEntityManagerFactoryBean factoryBean;
        factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ru.stoliarenkoas.tm.webserver.model");
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        final Properties properties = new Properties();
        properties.put(Environment.SHOW_SQL, showSql);
        properties.put(Environment.HBM2DDL_AUTO, tableStrategy);
        properties.put(Environment.DIALECT, dialect);

        properties.put(Environment.USE_SECOND_LEVEL_CACHE, useCache);
        properties.put(Environment.USE_QUERY_CACHE, useQueryCache);
        properties.put(Environment.CACHE_REGION_PREFIX, regionPrefix);
        properties.put(Environment.CACHE_PROVIDER_CONFIG, configFile);
        properties.put(Environment.CACHE_REGION_FACTORY, cacheFactoryClass);
        properties.put(Environment.IMPLICIT_NAMING_STRATEGY, implicitNamingStrategy);
        properties.put(Environment.PHYSICAL_NAMING_STRATEGY, physicalNamingStrategy);

        properties.put(Environment.USE_MINIMAL_PUTS, useMinPuts);
        //client does not contain any data
        properties.put("hibernate.cache.hazelcast.use_lite_member", useLiteMember);
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager(
            final LocalContainerEntityManagerFactoryBean factoryBean
    ) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(factoryBean.getObject());
        return transactionManager;
    }

}
