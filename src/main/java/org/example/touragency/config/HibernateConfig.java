package org.example.touragency.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.touragency.model.entity.Booking;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("0501");
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);

        return new HikariDataSource(config);
    }

    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.datasource", dataSource)
                .applySettings(hibernateProperties())
                .build();

        MetadataSources sources = new MetadataSources(registry);
        sources.addAnnotatedClass(org.example.touragency.model.entity.Tour.class);
        sources.addAnnotatedClass(org.example.touragency.model.entity.User.class);
        sources.addAnnotatedClass(org.example.touragency.model.entity.FavouriteTour.class);
        sources.addAnnotatedClass(org.example.touragency.model.entity.Booking.class);
        sources.addAnnotatedClass(org.example.touragency.model.entity.Rating.class);
        sources.addAnnotatedClass(org.example.touragency.model.entity.RatingCounter.class);

        return sources.buildMetadata()
                .buildSessionFactory();
    }

    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update");  // ← create/update/validate/none
        return props;
    }
}