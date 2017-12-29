package br.com.casadocodigoo.loja.conf;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
public class JPAConfiguration {

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter);

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUsername("root");
		dataSource.setPassword("q1w2e3r4");
		dataSource.setUrl("jdbc:mysql://localhost:3306/casadocodigoo");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");		

		factoryBean.setDataSource(dataSource);
		
		Properties props = new Properties();
		props.setProperty("hibernate.dialect" , "org.hibernate.dialect.MySQL5Dialect");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.hbm2ddl.auto", "update");
		factoryBean.setJpaProperties(props);

		factoryBean.setPackagesToScan("br.com.casadocodigoo.loja.models");

		return factoryBean;
	}

	@Bean
	public JpaTransactionManager TransactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
}