package com.endava.endabank.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false, length = 8)
    private String password;
    private  Float balance;
    @Column(nullable = false, length = 20)
    private String accountNumber;
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountType accountType;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToOne(mappedBy = "bankAccount")
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
