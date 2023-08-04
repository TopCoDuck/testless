package com.tetless.backend.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.tetless.backend.repository.disk.EscrowRepository;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableJpaRepositories(basePackageClasses = EscrowRepository.class)
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY)
@RequiredArgsConstructor
public class JpaConfig {

	private final Environment env;

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	DataSource escrowDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean
	EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
		return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
	}

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(
			@Qualifier("entityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
			@Qualifier("escrowDataSource") DataSource dataSource, JpaProperties jpaProperties) {
		return builder.dataSource(dataSource).packages(EscrowRepository.class).persistenceUnit("escrow")
				.properties(jpaProperties).build();
	}

	@Primary
	@Bean
	PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(entityManagerFactory);
		return tm;
	}

	@Bean
	JpaProperties jpaProperties() {
		String showSql = env.getProperty("spring.jpa.properties.hibernate.show_sql");
		String formatSql = env.getProperty("spring.jpa.properties.hibernate.format_sql");
		String useSqlComments = env.getProperty("spring.jpa.properties.hibernate.use_sql_comments");
		return new JpaProperties(showSql, formatSql, useSqlComments);
	}
}
