package com.alinesno.infra.base.search.memory.prompt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties
@Configuration
public class GetReflectionSubjectWorkerPrompt {

    private String get_reflection_subject_system ;
    private String get_reflection_subject_few_shot ;
    private String get_reflection_subject_user_query ;
}
