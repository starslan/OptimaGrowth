package com.optimagrowth.organizationservice.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimagrowth.organizationservice.model.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Producer {

    @Value("${topic.name}")
    private String orderTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public Producer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public String sendMessage(Organization organization) throws JsonProcessingException {
        String orderAsMessage = objectMapper.writeValueAsString(organization);
        kafkaTemplate.send(orderTopic, orderAsMessage);

        log.info("Change or create organization {}", orderAsMessage);

        return "message sent";
    }

    public String deleteOrgMessage(String orgId) throws JsonProcessingException {
        String orderAsMessage = objectMapper.writeValueAsString(orgId);
        kafkaTemplate.send(orderTopic, orderAsMessage);

        log.info("Delete organization {}", orderAsMessage);

        return "message sent";
    }
}
