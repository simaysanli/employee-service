package com.justeat.interview.employeeservice.services;

import com.justeat.interview.employeeservice.domain.model.AdditionalInfoDto;
import com.justeat.interview.employeeservice.entity.User;
import com.justeat.interview.employeeservice.repository.UserRepository;
import com.justeat.interview.employeeservice.util.AuthenticatedUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserDataService {

    private UserRepository userRepository;

    public Boolean updateAdditionalInfo(AdditionalInfoDto additionalInfoDto) {
        User authenticatedUser = new AuthenticatedUser(userRepository).getAuthenticatedUser();
        Integer numberOfRowsUpdated = userRepository.updateAdditionalInfo(additionalInfoDto.getAdditionalInfo(), authenticatedUser.getId());
        log.info("Affected number of rows: {} - Should be exactly one.", numberOfRowsUpdated);
        return numberOfRowsUpdated == 1;
    }

}
