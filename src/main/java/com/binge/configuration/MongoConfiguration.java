package com.binge.configuration;

import static com.google.common.collect.Collections2.transform;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("com.binge.repository")
public class MongoConfiguration {

    @Value("${mongodb.hosts}")
    private String mongodbHosts;

    @Value("${mongodb.dbname}")
    private String mongodbName;

    @Value("${mongodb.user}")
    private String mongodbUser;

    @Value("${mongodb.password}")
    private String mongodbPassword;

    @Bean(name="mongoDbFactory")
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        return getMongoFactory(mongodbName);
    }

    @Bean(name="mongoTemplate")
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        return new MongoTemplate(mongoDbFactory(), mongoConverter());
    }

    private MongoDbFactory getMongoFactory(String adminDbName) {
        SimpleMongoDbFactory factory = new SimpleMongoDbFactory(
                new MongoClient(getHostsAsServerAddresses(mongodbHosts), mongoCredentials()), adminDbName);
        factory.setWriteConcern(WriteConcern.MAJORITY);
        return factory;
    }

    @Bean
    public List<MongoCredential> mongoCredentials() {
        if (isNotBlank(mongodbUser) && isNotBlank(mongodbPassword)) {
            return Arrays.asList(MongoCredential.createCredential(mongodbUser, mongodbName,
                    mongodbPassword.toCharArray()));
        }
        return new ArrayList<MongoCredential>();
    }

    @Bean
    public MappingMongoConverter mongoConverter() throws UnknownHostException {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory()), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

    private List<ServerAddress> getHostsAsServerAddresses(String serverHosts) {
        List<String> splitHosts = Arrays.asList(serverHosts.split(","));
        return ImmutableList.copyOf(transform(splitHosts, (String hostPort) -> {
            String[] tokens = hostPort.split(":");
            String host = tokens[0];
            int port = (tokens.length > 1) ? Integer.parseInt(tokens[1]) : 27017;
            return new ServerAddress(host, port);
        }));
    }

}

