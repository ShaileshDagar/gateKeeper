package com.GateKeeper.gateKeeper.domain;

import com.GateKeeper.gateKeeper.model.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"community_name", "address_area_code"})})
public class Community {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityID;

    @Column(name = "community_name",nullable = false)
    private String communityName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "areaCode", column = @Column(name = "address_area_code")),
            @AttributeOverride( name = "city", column = @Column(name = "address_city")),
            @AttributeOverride( name = "state", column = @Column(name = "address_state")),
            @AttributeOverride( name = "country", column = @Column(name = "address_country"))
    })
    private Address communityAddress;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "community_user",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> communityUsers;

    @OneToMany(mappedBy = "flatCommunity", cascade = {CascadeType.ALL})//May be better to keep it to 'persist' & 'merge'
    private Set<Flat> communityFlats;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Community community)) return false;
        return communityID.equals(community.communityID)
                && communityName.equals(community.communityName)
                && Objects.equals(communityAddress, community.communityAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(communityID, communityName, communityAddress);
    }
}
