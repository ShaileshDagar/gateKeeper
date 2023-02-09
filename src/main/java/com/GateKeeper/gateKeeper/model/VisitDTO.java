package com.GateKeeper.gateKeeper.model;

import com.GateKeeper.gateKeeper.domain.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitDTO {
    @NotEmpty
    private FlatDTO visitFlatDTO;

    @NotEmpty
    private User visitingUser;

    @NotEmpty
    private LocalDateTime visitInBoundTime;

    private LocalDateTime visitOutBoundTime;
}
