package com.kurban.KConsumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class KafkaConsumer {
    @Bean
    Consumer<OrderRequestDTO> processOrderRequest(){
        return System.out::println;
    }
}
