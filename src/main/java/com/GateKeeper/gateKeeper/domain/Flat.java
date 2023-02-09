package com.GateKeeper.gateKeeper.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"flatNumber", "community_id"}))
public class Flat implements Serializable {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flatID;

    @Column(name = "flatNumber", nullable = false)
    private String flatNumber;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community flatCommunity;

    @ManyToMany(mappedBy = "userFlats")
    private Set<User> flatUsers;

    @OneToMany(mappedBy = "visitFlat")
    private List<Visit> flatVisits;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flat flat)) return false;
        return flatNumber.equals(flat.flatNumber) && flatCommunity.equals(flat.flatCommunity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatNumber, flatCommunity);
    }
}
