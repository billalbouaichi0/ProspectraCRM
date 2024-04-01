package com.prospectra.bp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, "oAuth2Authentication");
            createCache(cm, com.prospectra.bp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.prospectra.bp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.prospectra.bp.domain.User.class.getName());
            createCache(cm, com.prospectra.bp.domain.Authority.class.getName());
            createCache(cm, com.prospectra.bp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.prospectra.bp.domain.Emails.class.getName());
            createCache(cm, com.prospectra.bp.domain.Phones.class.getName());
            createCache(cm, com.prospectra.bp.domain.Contacts.class.getName());
            createCache(cm, com.prospectra.bp.domain.Contacts.class.getName() + ".emails");
            createCache(cm, com.prospectra.bp.domain.Contacts.class.getName() + ".phones");
            createCache(cm, com.prospectra.bp.domain.Contacts.class.getName() + ".tags");
            createCache(cm, com.prospectra.bp.domain.Contacts.class.getName() + ".companies");
            createCache(cm, com.prospectra.bp.domain.Companies.class.getName());
            createCache(cm, com.prospectra.bp.domain.Companies.class.getName() + ".emails");
            createCache(cm, com.prospectra.bp.domain.Companies.class.getName() + ".tags");
            createCache(cm, com.prospectra.bp.domain.Companies.class.getName() + ".contacts");
            createCache(cm, com.prospectra.bp.domain.Opportunities.class.getName());
            createCache(cm, com.prospectra.bp.domain.Opportunities.class.getName() + ".tasks");
            createCache(cm, com.prospectra.bp.domain.Opportunities.class.getName() + ".notes");
            createCache(cm, com.prospectra.bp.domain.Opportunities.class.getName() + ".products");
            createCache(cm, com.prospectra.bp.domain.Stages.class.getName());
            createCache(cm, com.prospectra.bp.domain.Stages.class.getName() + ".opportunities");
            createCache(cm, com.prospectra.bp.domain.Pipelines.class.getName());
            createCache(cm, com.prospectra.bp.domain.Pipelines.class.getName() + ".stages");
            createCache(cm, com.prospectra.bp.domain.Products.class.getName());
            createCache(cm, com.prospectra.bp.domain.Products.class.getName() + ".opportunities");
            createCache(cm, com.prospectra.bp.domain.Tasks.class.getName());
            createCache(cm, com.prospectra.bp.domain.Notes.class.getName());
            createCache(cm, com.prospectra.bp.domain.Tags.class.getName());
            createCache(cm, com.prospectra.bp.domain.Tags.class.getName() + ".contacts");
            createCache(cm, com.prospectra.bp.domain.Tags.class.getName() + ".companies");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
