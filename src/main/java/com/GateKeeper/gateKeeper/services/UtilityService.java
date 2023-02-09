package com.GateKeeper.gateKeeper.services;

import com.GateKeeper.gateKeeper.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

@Service
public class UtilityService {
    private static final Logger logger = LoggerFactory.getLogger(UtilityService.class);

    public User getCurrentPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : (User) authentication.getPrincipal();
    }
    public boolean errorsFound(BindingResult bindingResult){
        boolean errorsFound = bindingResult.hasErrors();
        if(errorsFound){
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::toString).toList();
            logger.error("Validation Constraint(s) are not met");
            for(String error : errors)
                logger.error(error);
        }
        return errorsFound;
    }
}
