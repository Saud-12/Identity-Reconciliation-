package com.bitespeed.identityReconciliation.repository;

import com.bitespeed.identityReconciliation.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer> {
    List<Contact> findByEmailOrPhoneNumberOrderByCreatedAtAsc(String email, String phoneNumber);

    List<Contact> findByEmail(String email);

    List<Contact> findByPhoneNumber(String phoneNumber);

    List<Contact> findAllByLinkedIdOrId(Integer id, Integer id1);

    List<Contact> findAllByLinkedId(Integer id);
}
