package com.example.rbac.user_management_rbac.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.events.exchange:rbac.events}")
    private String exchangeName;

    public static final String REGISTRATION_QUEUE = "rbac.user.registered.queue";
    public static final String LOGIN_QUEUE = "rbac.user.login.queue";

    public static final String REGISTRATION_ROUTING_KEY = "rbac.user.registered";
    public static final String LOGIN_ROUTING_KEY = "rbac.user.login";

    @Bean
    public TopicExchange rbacExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue registrationQueue() {
        return new Queue(REGISTRATION_QUEUE, true);
    }

    @Bean
    public Queue loginQueue() {
        return new Queue(LOGIN_QUEUE, true);
    }

    @Bean
    public Binding registrationBinding(Queue registrationQueue, TopicExchange rbacExchange) {
        return BindingBuilder.bind(registrationQueue)
                .to(rbacExchange)
                .with(REGISTRATION_ROUTING_KEY);
    }

    @Bean
    public Binding loginBinding(Queue loginQueue, TopicExchange rbacExchange) {
        return BindingBuilder.bind(loginQueue)
                .to(rbacExchange)
                .with(LOGIN_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
