package com.example.rbac.user_management_rbac.services;


import com.example.rbac.user_management_rbac.DTO.UserEvent;
import com.example.rbac.user_management_rbac.entity.User;
import org.springframework.stereotype.Service;
import com.example.rbac.user_management_rbac.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherService {
    private final RabbitTemplate rabbitTemplate;
    @Value("${app.events.exchange:rbac.events}")
    private String exchangeName;

    public EventPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserRegistered(User user) {
        UserEvent event = new UserEvent(
                "USER_REGISTERED",
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                System.currentTimeMillis()
        );

        rabbitTemplate.convertAndSend(
                exchangeName,
                RabbitMQConfig.REGISTRATION_ROUTING_KEY,
                event
        );
    }

    public void publishUserLogin(User user) {
        UserEvent event = new UserEvent(
                "USER_LOGIN",
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                System.currentTimeMillis()
        );

        rabbitTemplate.convertAndSend(
                exchangeName,
                RabbitMQConfig.LOGIN_ROUTING_KEY,
                event
        );
    }

}
