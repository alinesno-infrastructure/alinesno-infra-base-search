package com.alinesno.infra.base.search.memory.prompt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties
@Configuration
public class GetObservationWorkerPrompt {

    private String get_observation_system ;
    private String get_observation_few_shot ;
    private String get_observation_user_query ;
}
