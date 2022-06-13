package com.endava.endabank.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "state_types")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 40, nullable = false)
    private String name;

    @OneToMany(mappedBy = "stateType", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StateType stateType = (StateType) o;
        return id != null && Objects.equals(id, stateType.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
