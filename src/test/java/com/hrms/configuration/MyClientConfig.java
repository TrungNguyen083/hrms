package com.hrms.configuration;

import com.hrms.search.configuration.SearchClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ComponentScan(basePackages = {"com.hrms.search"})
@EnableElasticsearchRepositories(basePackages = "com.hrms.search.repository")
public class MyClientConfig extends SearchClientConfig {
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9201")
                .build();
    }
}
