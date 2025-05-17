package com.bitespeed.identityReconciliation.service;

import com.bitespeed.identityReconciliation.dto.ContactLookupRequest;
import com.bitespeed.identityReconciliation.dto.ContactLookupResponse;
import com.bitespeed.identityReconciliation.entity.Contact;
import com.bitespeed.identityReconciliation.entity.eums.LinkPrecedence;
import com.bitespeed.identityReconciliation.exceptions.NoContactInfoException;
import com.bitespeed.identityReconciliation.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactLookupResponse createContact(ContactLookupRequest request) {

        if(request.getEmail()==null && request.getPhoneNumber()==null){
            throw new NoContactInfoException("Request must contain either email or phoneNumber");
        }
        List<Contact> contacts = contactRepository
                .findByEmailOrPhoneNumberOrderByCreatedAtAsc(request.getEmail(), request.getPhoneNumber());

        Contact primaryContact;

        if (contacts.isEmpty()) {
            // No contact exists – create a new primary
            Contact newContact = contactRepository.save(Contact.builder()
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .linkPrecedence(LinkPrecedence.PRIMARY)
                    .build());

            return buildResponse(newContact, List.of());
        }

        // Step 1: Identify all primaries
        Set<Contact> primaries = contacts.stream()
                .filter(c -> c.getLinkPrecedence() == LinkPrecedence.PRIMARY)
                .collect(Collectors.toSet());

        // Step 2: Merge if more than one primary
        primaryContact = contacts.get(0); // since it's ordered by createdAt ASC
        if (primaryContact.getLinkPrecedence() == LinkPrecedence.SECONDARY) {
            primaryContact = contactRepository.findById(primaryContact.getLinkedId()).orElseThrow();
        }

        for (Contact primary : primaries) {
            if (!primary.getId().equals(primaryContact.getId())) {
                // This is another primary – convert it to secondary and relink
                primary.setLinkPrecedence(LinkPrecedence.SECONDARY);
                primary.setLinkedId(primaryContact.getId());
                contactRepository.save(primary);

                // Update all secondaries pointing to the old primary
                List<Contact> toUpdate = contactRepository.findAllByLinkedId(primary.getId());
                for (Contact c : toUpdate) {
                    c.setLinkedId(primaryContact.getId());
                    contactRepository.save(c);
                }
            }
        }

        List<Contact> allLinked = contactRepository
                .findAllByLinkedIdOrId(primaryContact.getId(), primaryContact.getId());

        if(!contactRepository.findByEmail(request.getEmail()).isEmpty() && !contactRepository.findByPhoneNumber(request.getPhoneNumber()).isEmpty()){
            return buildResponse(primaryContact, allLinked);
        }

        // Step 3: Check if the incoming data is a new combination
        boolean alreadyExists = contacts.stream().anyMatch(c ->
                Objects.equals(c.getEmail(), request.getEmail()) &&
                        Objects.equals(c.getPhoneNumber(), request.getPhoneNumber())
        );

        Contact newSecondary = null;
        if (!alreadyExists) {
            newSecondary = contactRepository.save(Contact.builder()
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .linkedId(primaryContact.getId())
                    .linkPrecedence(LinkPrecedence.SECONDARY)
                    .build());
        }

        // Step 4: Fetch all linked contacts (primary + secondaries)


        if (newSecondary != null) {
            allLinked.add(newSecondary);
        }

        return buildResponse(primaryContact, allLinked);
    }

    private ContactLookupResponse buildResponse(Contact primary, List<Contact> allContacts) {
        Set<String> emails = new LinkedHashSet<>();
        Set<String> phoneNumbers = new LinkedHashSet<>();
        Set<Integer> secondaryIds = new HashSet<>();

        if (primary.getEmail() != null) emails.add(primary.getEmail());
        if (primary.getPhoneNumber() != null) phoneNumbers.add(primary.getPhoneNumber());

        for (Contact c : allContacts) {
            if (!c.getId().equals(primary.getId())) {
                if (c.getEmail() != null) emails.add(c.getEmail());
                if (c.getPhoneNumber() != null) phoneNumbers.add(c.getPhoneNumber());
                secondaryIds.add(c.getId());
            }
        }

        return ContactLookupResponse.builder()
                .primaryContactId(primary.getId())
                .emails(emails)
                .phoneNumbers(phoneNumbers)
                .secondaryContactIds(secondaryIds)
                .build();
    }
}
