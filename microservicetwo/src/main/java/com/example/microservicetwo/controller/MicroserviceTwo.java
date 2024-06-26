package com.example.microservicetwo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.jms.*;

@RestController
@Slf4j
public class MicroserviceTwo
{
    private static String url= ActiveMQConnection.DEFAULT_BROKER_URL;

    private static String queueName="Queue_3";

    @GetMapping("/func2")
    public String func2() throws JMSException {

        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory(url);

        Connection connection=connectionFactory.createConnection();

        connection.start();

        Session session= connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        Destination destination=session.createQueue(queueName);

        MessageConsumer consumer=session.createConsumer(destination);

        Message message=consumer.receive();

        if(message instanceof TextMessage)
        {
            TextMessage message1=(TextMessage) message;
            log.info("Message from : {}",message1.getText());
        }
        log.info("Func2");
        return "Func2";
    }

}

