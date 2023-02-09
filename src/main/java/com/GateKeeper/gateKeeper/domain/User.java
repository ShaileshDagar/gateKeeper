package com.GateKeeper.gateKeeper.domain;

import com.GateKeeper.gateKeeper.model.Address;
import com.GateKeeper.gateKeeper.model.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long userID;

    @Column(nullable = false, unique = true)
    protected String username;

    @Column(nullable = false, unique = true, length = 50)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "areaCode", column = @Column(name = "address_area_code")),
            @AttributeOverride( name = "city", column = @Column(name = "address_city")),
            @AttributeOverride( name = "state", column = @Column(name = "address_state")),
            @AttributeOverride( name = "country", column = @Column(name = "address_country"))
    })
    protected Address userAddress;

    @Enumerated(EnumType.STRING)
    protected Role userRole;

    @ManyToMany(mappedBy = "communityUsers")
    protected Set<Community> userCommunities;

    @ManyToMany
    @JoinTable(
            name = "user_flat",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "flat_id")
    )
    protected Set<Flat> userFlats;

    @OneToMany(mappedBy = "visitingUser")
    protected List<Visit> userVisits;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    protected OffsetDateTime lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
