package com.bitespeed.identityReconciliation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactLookupRequest {
    @Email(message = "Invalid email")
    String email;

    @Pattern(regexp = "^(\\+91[-\\s]?|91[-\\s]?|0)?[6-9]\\d{9}$", message = "Invalid Indian phone number")
    private String phoneNumber;
}
