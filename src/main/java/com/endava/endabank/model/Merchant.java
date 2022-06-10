package com.endava.endabank.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "merchants")
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String taxId;

    private String apiId;

    private String merchantKey;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime createAt;

    private LocalDateTime updatedAt;

    @Column( nullable = false)
    private String storeName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "merchant_request_status_id", nullable = false)
    private MerchantRequestState merchantRequestState;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "reviewing_user_id")
    private User reviewedBy;

    @PrePersist
    private void beforeSaving() {
        createAt = LocalDateTime.now();
    }

    @PreUpdate
    private void beforeUpdating() {
        updatedAt = LocalDateTime.now();
    }




}
