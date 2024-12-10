package com.alinesno.infra.base.search.memory.prompt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties
@Configuration
public class GetObservationWithTimeWorkerPrompt {

    private String time_string_format ;
    private String get_observation_with_time_few_shot ;
    private String get_observation_with_time_user_query ;


}
