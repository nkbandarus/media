package com.binge.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.xml.transform.Source;

import java.util.List;

@Import({ MongoConfiguration.class})
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.binge.service", "com.binge.controller","com.binge.repository"}, useDefaultFilters = false, excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ApplicationConfiguration.class) }, includeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {
        Controller.class, Component.class}) })
@PropertySource("classpath:mongo-db.properties")
public class ApplicationConfiguration extends WebMvcConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public TomcatServletWebServerFactory tomcatEmbeddedServletContainerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        converters.add(converter);

        addDefaultHttpMessageConverters(converters);
    }

    private void addDefaultHttpMessageConverters(
            List<HttpMessageConverter<?>> messageConverters) {
        // as per WebMvcConfigurationSupport.addDefaultHttpMessageConverters()

        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);

        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(stringConverter);
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new SourceHttpMessageConverter<Source>());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
    }
}
