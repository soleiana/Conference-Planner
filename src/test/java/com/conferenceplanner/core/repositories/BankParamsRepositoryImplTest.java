package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.BankParams;
import com.conferenceplanner.core.domain.BankParamsFixture;
import com.conferenceplanner.core.repositories.tools.DBCleaner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Ann on 09/09/14.
 */
public class BankParamsRepositoryImplTest extends SpringContextTest
{
    @Autowired
    private DBCleaner dbCleaner;
    @Autowired
    private BankParamsRepository bankParamsRepository;
    private BankParams bankParams;

    @Before
    @Transactional
    public void setUp()
    {
        dbCleaner.clear();
        bankParams = BankParamsFixture.standardBankParams();
    }

    @Test
    @Transactional
    public void testCreate()
    {
        bankParamsRepository.create(bankParams);
        assertNotNull(bankParams.getId());
    }

    @Test
    @Transactional
    public void testGetById()
    {
        bankParamsRepository.create(bankParams);
        Integer id = bankParams.getId();
        assertEquals(bankParams, bankParamsRepository.getById(id));
    }

    @Test
    @Transactional
    public void testUpdate()
    {
        bankParamsRepository.create(bankParams);
        BankParams newBankParams = BankParamsFixture.newBankParams();

        bankParams.setMaxLoanAmount(newBankParams.getMaxLoanAmount());
        bankParams.setBaseInterestRate(newBankParams.getBaseInterestRate());
        bankParams.setInterestRateFactor(newBankParams.getInterestRateFactor());
        bankParams.setMaxLoanAttempts(newBankParams.getMaxLoanAttempts());
        bankParams.setRiskTimeStart(newBankParams.getRiskTimeStart());
        bankParams.setRiskTimeEnd(newBankParams.getRiskTimeEnd());
        bankParams.setLoanExtensionTerm(newBankParams.getLoanExtensionTerm());

        bankParamsRepository.update(bankParams);

        assertEquals(newBankParams.getMaxLoanAmount(), bankParams.getMaxLoanAmount());
        assertEquals(newBankParams.getBaseInterestRate(), bankParams.getBaseInterestRate());
        assertEquals(newBankParams.getInterestRateFactor(), bankParams.getInterestRateFactor());
        assertEquals(newBankParams.getMaxLoanAttempts(), bankParams.getMaxLoanAttempts());
        assertEquals(newBankParams.getRiskTimeStart(), bankParams.getRiskTimeStart());
        assertEquals(newBankParams.getRiskTimeEnd(), bankParams.getRiskTimeEnd());
        assertEquals(newBankParams.getLoanExtensionTerm(), bankParams.getLoanExtensionTerm());
    }

    @Test
    @Transactional
    public void testGetLast()
    {
        BankParams bp1 = BankParamsFixture.standardBankParams();
        bp1.setLoanExtensionTerm(BankParamsFixture.NEW_LOAN_EXTENSION_TERM);
        BankParams bp2 = BankParamsFixture.standardBankParams();
        bp2.setInterestRateFactor(BankParamsFixture.NEW_INTEREST_RATE_FACTOR);

        bankParamsRepository.create(bankParams);
        bankParamsRepository.create(bp1);
        bankParamsRepository.create(bp2);

        BankParams bp = bankParamsRepository.getLast();
        assertEquals(bp2,bp);
    }
}
