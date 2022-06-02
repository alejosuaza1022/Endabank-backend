package com.endava.endabank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
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

    @Column(nullable = false)
    private String apiId;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column( nullable = false)
    private LocalDateTime updatedAt;

    private String storeName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "merchant_request_status_id", nullable = false)
    private MerchantRequestState merchantRequestState;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
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
