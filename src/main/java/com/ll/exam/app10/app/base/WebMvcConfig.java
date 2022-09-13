package com.ll.exam.app10.app.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/gen/**") // 이렇게 요청이 들어오면 ex. http://localhost:8010/gen-file/1.png
                .addResourceLocations("file:///" + genFileDirPath + "/"); // 파일을 이 경로(genFileDirPath)에서 찾는다
    }
}
