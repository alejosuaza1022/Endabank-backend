package com.endava.endabank.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "merchant_request_states")
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MerchantRequestState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "merchantRequestState", orphanRemoval = true)
    @ToString.Exclude
    private List<Merchant> users = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MerchantRequestState merchantRequestState = (MerchantRequestState) o;
        return id != null && Objects.equals(id, merchantRequestState.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
