package com.endava.endabank.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "cards")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column( length = 8)
    private String cvv;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "bank_account_id",nullable = false)
    private BankAccount bankAccount;

    @Column(nullable = false, length = 20)
    private String nameOnCard;

    @Column(nullable = false, length = 20)
    private String cardNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Card card = (Card) o;
        return id != null && Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
