package com.endava.endabank.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Column(nullable = false)
    private Double amount;

    @Column(length = 40)
    private String address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bank_account_issuer_id")
    private BankAccount bankAccountIssuer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bank_account_receiver_id")
    private BankAccount bankAccountReceiver;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transaction_type_id", nullable = false)
    private TransactionType transactionType;

    @Column(name = "state_description")
    private String stateDescription;

    @ManyToOne(optional = false)
    @JoinColumn(name = "state_type_id", nullable = false)
    private StateType stateType;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @PrePersist
    private void beforeSaving() {
        createAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Transaction that = (Transaction) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
