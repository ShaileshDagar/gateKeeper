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
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Visit implements Serializable {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitID;

    @Column(nullable = false, updatable = false)
    private LocalDateTime visitInBoundTime;

    private LocalDateTime visitOutBoundTime;

    @ManyToOne
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat visitFlat;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User visitingUser;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Visit visit)) return false;
        return visitID.equals(visit.visitID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitID);
    }
}
