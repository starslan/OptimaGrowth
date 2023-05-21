package com.optimagrowth.licenseservice.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("organization")
public class Organization {

    String id;
    String name;
    String contactName;
    String contactEmail;
    String contactPhone;
}
