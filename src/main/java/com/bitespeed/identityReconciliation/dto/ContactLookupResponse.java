package com.bitespeed.identityReconciliation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactLookupResponse {
    private Integer primaryContactId;
    private Set<String> emails;
    private Set<String> phoneNumbers;
    private Set<Integer> secondaryContactIds;
}
