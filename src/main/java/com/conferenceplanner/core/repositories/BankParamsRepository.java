package com.conferenceplanner.core.repositories;

import com.conferenceplanner.core.domain.BankParams;


/**
 * Created by Ann on 06/09/14.
 */
public interface BankParamsRepository
{
    void create(BankParams bankParams);

    BankParams getById(Integer id);

    BankParams getLast();

    void update(BankParams bankParams);
}
