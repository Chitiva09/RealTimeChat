package com.chat.config.kafka.producerKafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerAdminConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    //configuracion del servidor de arranque de kafka
    @Bean
    public KafkaAdmin kafkaAdmin(){

        Map<String, Object> configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaAdmin.NewTopics topics(){
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("canal-de-la-mancha").partitions(2).replicas(1).build()
        );
    }

}
