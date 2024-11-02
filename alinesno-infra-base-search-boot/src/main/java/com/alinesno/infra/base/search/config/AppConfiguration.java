package com.alinesno.infra.base.search.config;

import com.alinesno.infra.base.search.service.IVectorDatasetService;
import com.alinesno.infra.common.facade.enable.EnableActable;
import com.alinesno.infra.common.web.adapter.sso.enable.EnableInfraSsoApi;
import com.alinesno.infra.common.web.log.aspect.LogAspect;
import com.dtflys.forest.springboot.annotation.ForestScan;
import jakarta.servlet.MultipartConfigElement;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.unit.DataSize;

@Slf4j
@EnableScheduling
@EnableActable
@EnableInfraSsoApi
@MapperScan("com.alinesno.infra.base.search.mapper")
@ForestScan({
        "com.alinesno.infra.common.web.adapter.base.consumer" ,
        "com.alinesno.infra.base.search.adapter.consumer"
})
@Configuration
public class AppConfiguration implements CommandLineRunner {

    @Autowired
    private IVectorDatasetService vectorDatasetService ;

    /**
     * 配置上传文件大小
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {

        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(100));
        factory.setMaxRequestSize(DataSize.ofMegabytes(100));

        return factory.createMultipartConfig();
    }

    @Bean
    public LogAspect getLogAspect(){
        return new LogAspect() ;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("当前向量存储引擎:{}" , vectorDatasetService.getVectorEngine());
    }
}
