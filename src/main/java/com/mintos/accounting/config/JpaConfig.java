package com.mintos.accounting.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeAware")
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AnonymousAuditorAware();
    }

    @ConditionalOnMissingBean(DateTimeProvider.class)
    @Bean
    public DateTimeProvider dateTimeAware() {
        return new DateTimeAware();
    }
}
