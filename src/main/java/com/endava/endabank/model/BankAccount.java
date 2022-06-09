package com.endava.endabank.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String password;

    private Double balance;

    @Column(nullable = false)
    private BigInteger accountNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountType accountType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private Card card;

    @OneToMany(mappedBy = "bankAccountIssuer")
    @ToString.Exclude
    private List<Transaction> transactionsSent;

    @OneToMany(mappedBy = "bankAccountReceiver")
    @ToString.Exclude
    private List<Transaction> transactionsReceived;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BankAccount that = (BankAccount) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
