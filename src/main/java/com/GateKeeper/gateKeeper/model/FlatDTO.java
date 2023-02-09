package com.GateKeeper.gateKeeper.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlatDTO {
    @NotEmpty
    private String flatNumber;

    @NotNull
    private CommunityDTO communityDTO;
}
