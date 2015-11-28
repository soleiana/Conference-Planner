package com.conferenceplanner.core.services.helpers;

import com.conferenceplanner.core.domain.BankParams;
import com.conferenceplanner.core.repositories.BankParamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Ann on 08/10/14.
 */
@Component
public abstract class ConstraintChecker
{
    @Autowired
    private BankParamsRepository bankParamsRepository;

    protected  BankParams getBankParams()
    {
        return bankParamsRepository.getLast();
    }
}
