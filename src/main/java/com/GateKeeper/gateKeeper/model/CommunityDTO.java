package com.GateKeeper.gateKeeper.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityDTO {

    @NotNull
    @Size(max = 255)
    private String communityName;

    @NotNull
    private Address communityAddress;

    private Set<String> communityUsers;
}
