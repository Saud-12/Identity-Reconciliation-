package com.bitespeed.identityReconciliation.controller;

import com.bitespeed.identityReconciliation.dto.ContactLookupRequest;
import com.bitespeed.identityReconciliation.dto.ContactLookupResponse;
import com.bitespeed.identityReconciliation.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    @PostMapping("/identify")
    public ResponseEntity<ContactLookupResponse> createContact(@RequestBody @Valid ContactLookupRequest contactLookupRequest){
        return new ResponseEntity<>(contactService.createContact(contactLookupRequest), HttpStatus.CREATED);
    }
}
