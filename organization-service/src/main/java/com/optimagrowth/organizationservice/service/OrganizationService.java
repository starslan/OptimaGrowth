package com.optimagrowth.organizationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.optimagrowth.organizationservice.model.Organization;
import com.optimagrowth.organizationservice.repository.OrganizationRepository;
import com.optimagrowth.organizationservice.service.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository repository;

    @Autowired
    Producer producer;

    public Organization findById(String organizationId) {
        Optional<Organization> opt = repository.findById(organizationId);
        return opt.orElse(null);
    }

    public Organization create(Organization organization){
        organization.setId( UUID.randomUUID().toString());
        organization = repository.save(organization);
        try {
            producer.sendMessage(organization);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return organization;

    }

    public void update(Organization organization){
        repository.save(organization);
        try {
            producer.sendMessage(organization);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void delete(String organizationId){
        repository.deleteById(organizationId);
        try {
            producer.deleteOrgMessage(organizationId);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
