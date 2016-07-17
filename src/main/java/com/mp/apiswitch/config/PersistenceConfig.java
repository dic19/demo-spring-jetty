package com.mp.apiswitch.config;

import static com.mp.apiswitch.statics.SpringProfiles.*;
import javax.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.Profile;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Delcio Amarillo
 */
@Configuration
@EnableTransactionManagement
@EnableLoadTimeWeaving
public class PersistenceConfig {
    
    @Bean
    public LoadTimeWeaver loadTimeWeaver() {
        return new InstrumentationLoadTimeWeaver();
    }
    
    @Bean
    @Profile(PRODUCTION)
    public LocalContainerEntityManagerFactoryBean prodEntityManagerFactory(LoadTimeWeaver loadTimeWeaver) {
        return entityManagerFactory("ProductionPU", loadTimeWeaver);
    }
    
    @Bean
    @Profile(DEVELOPMENT)
    public LocalContainerEntityManagerFactoryBean devEntityManagerFactory(LoadTimeWeaver loadTimeWeaver) {
        return entityManagerFactory("DevelopmentPU", loadTimeWeaver);
    }
    
    @Bean
    @Profile(TEST)
    public LocalContainerEntityManagerFactoryBean testEntityManagerFactory(LoadTimeWeaver loadTimeWeaver) {
        return entityManagerFactory("TestPU", loadTimeWeaver);
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(LoadTimeWeaver loadTimeWeaver) {
        return entityManagerFactory("DevelopmentPU", loadTimeWeaver);
    }
    
    private LocalContainerEntityManagerFactoryBean entityManagerFactory(String persistenceUnitName, LoadTimeWeaver loadTimeWeaver) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceUnitName(persistenceUnitName);
        factory.setLoadTimeWeaver(loadTimeWeaver);
        return factory;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }    
}
