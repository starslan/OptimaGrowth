package com.optimagrowth.licenseservice.events.model;

import lombok.Data;

@Data
public class OrganizationChangeModel {

    private String type;
    private String action;
    private String organizationId;
    private String correlationId;
}
