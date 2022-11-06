package com.optimagrowth.licenseservice.service;

import com.optimagrowth.licenseservice.config.ServiceConfig;
import com.optimagrowth.licenseservice.model.License;
import com.optimagrowth.licenseservice.model.Organization;
import com.optimagrowth.licenseservice.repository.LicenseRepository;
import com.optimagrowth.licenseservice.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.licenseservice.service.client.OrganizationFeignClient;
import com.optimagrowth.licenseservice.service.client.OrganizationRestTemplateClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
public class LicenseService {

    @Autowired
    MessageSource messages;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationRestTemplateClient organizationRestClient;

    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;

    @Autowired
    OrganizationFeignClient organizationFeignClient;


    private final Logger logger = LoggerFactory.getLogger(License.class);


    private void randomlyRunLong() throws TimeoutException{
        Random random = new Random();
        int randomNum = random.nextInt(3) + 1;
//        if(randomNum == 3){
            sleep();
//        }

    }

    private void sleep() throws TimeoutException {
        try {
            Thread.sleep(5000);
//            Thread.sleep(111000);
            throw new TimeoutException();
        }catch (InterruptedException e){
            logger.error(e.getMessage());
        }
    }


    public License getLicense(String licenseId, String organizationId, String clientType){
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (null == license) {
            throw new IllegalArgumentException(String.format(messages.getMessage("license.search.error.message", null, null),licenseId, organizationId));
        }

        Organization organization = this.retrieveOrganization(organizationId, clientType);
        if(organization != null){
            license.setOrganizationName(organization.getName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactName(organization.getContactName());
            license.setContactPhone(organization.getContactName());
        }

        return license.withComment(config.getProperty());
    }

    private Organization retrieveOrganization(String organizationId, String clientType){

        Organization organization = null;

        switch (clientType){

            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;

            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
        }



        return organization;
    }

    public License createLicense(License license){
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license){
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId){
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messages.getMessage("license.delete.message", null, null),licenseId);
        return responseMessage;

    }


    @CircuitBreaker(name="licenseService")
    public List<License> getLicenses(String organizationId) throws TimeoutException {
        randomlyRunLong();

        return  licenseRepository.findByOrganizationId(organizationId);
    }



}
