package com.bitespeed.identityReconciliation.entity;

import com.bitespeed.identityReconciliation.entity.eums.LinkPrecedence;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="contact")
public class Contact {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String phoneNumber;
    private String email;
    private Integer linkedId;

    @Enumerated(EnumType.STRING)
    private LinkPrecedence linkPrecedence;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
