package com.optimagrowth.licenseservice.controller;

import com.optimagrowth.licenseservice.model.License;
import com.optimagrowth.licenseservice.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="v1/organization/{organizationId}/license")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @RequestMapping(value="/{licenseId}",method = RequestMethod.GET)
    public ResponseEntity<License> getLicense( @PathVariable("organizationId") String organizationId,
                                               @PathVariable("licenseId") String licenseId) {

        License license = licenseService.getLicense(licenseId, organizationId, "");
//        license.add(
//                linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId())).withSelfRel(),
//                linkTo(methodOn(LicenseController.class).createLicense(license)).withRel("createLicense"),
//                linkTo(methodOn(LicenseController.class).updateLicense(license)).withRel("updateLicense"),
//                linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId())).withRel("deleteLicense")
//        );

        return ResponseEntity.ok(license);
    }

    @RequestMapping(value = "/{licenseId}/{clientId}")
    public License getLicenceWithClient(
            @PathVariable ("organizationId") String organizationId,
            @PathVariable ("licenseId") String licenseId,
            @PathVariable ("clientId") String clientId

    ){

        return licenseService.getLicense(licenseId, organizationId, clientId);
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(@RequestBody License request) {
        System.out.println("PutMapping");
        return ResponseEntity.ok(licenseService.updateLicense(request));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License request) {
        System.out.println("PostMapping");
        return ResponseEntity.ok(licenseService.createLicense(request));
    }

    @DeleteMapping(value="/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }



}
