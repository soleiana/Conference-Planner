package com.conferenceplanner.core.domain;

import java.math.BigDecimal;
import  java.sql.Time;

/**
 * Created by Ann on 10/09/14.
 */
public class BankParamsFixture
{
    private static final BigDecimal STANDARD_MAX_LOAN_AMOUNT = new BigDecimal("4000.00");
    private static final BigDecimal STANDARD_BASE_INTEREST_RATE = new BigDecimal("100.00");
    private static final BigDecimal STANDARD_INTEREST_RATE_FACTOR = new BigDecimal("1.50");
    private static final Byte STANDARD_MAX_LOAN_ATTEMPTS = new Byte("3");
    private static final Time STANDARD_RISK_TIME_START = Time.valueOf("00:00:00");
    private static final Time STANDARD_RISK_TIME_END = Time.valueOf("07:00:00");
    private static final Short STANDARD_lOAN_EXTENSION_TERM = new Short("7");

    public static final BigDecimal NEW_MAX_LOAN_AMOUNT = new BigDecimal("5000.00");
    public static final BigDecimal NEW_BASE_INTEREST_RATE = new BigDecimal("150.00");
    public static final BigDecimal NEW_INTEREST_RATE_FACTOR = new BigDecimal("2.00");
    public static final Byte NEW_MAX_LOAN_ATTEMPTS = new Byte("5");
    public static final Time NEW_RISK_TIME_START = Time.valueOf("22:00:00");
    public static final Time NEW_RISK_TIME_END = Time.valueOf("08:00:00");
    public static final Short NEW_LOAN_EXTENSION_TERM = new Short("14");

    public static BankParams standardBankParams()
    {
        BankParams bankParams = new BankParams();
        bankParams.setMaxLoanAmount(STANDARD_MAX_LOAN_AMOUNT);
        bankParams.setBaseInterestRate(STANDARD_BASE_INTEREST_RATE);
        bankParams.setInterestRateFactor(STANDARD_INTEREST_RATE_FACTOR);
        bankParams.setMaxLoanAttempts(STANDARD_MAX_LOAN_ATTEMPTS);
        bankParams.setRiskTimeStart(STANDARD_RISK_TIME_START);
        bankParams.setRiskTimeEnd(STANDARD_RISK_TIME_END);
        bankParams.setLoanExtensionTerm(STANDARD_lOAN_EXTENSION_TERM);
        return bankParams;
    }

    public static BankParams newBankParams()
    {
        BankParams bankParams = new BankParams();
        bankParams.setMaxLoanAmount(NEW_MAX_LOAN_AMOUNT);
        bankParams.setBaseInterestRate(NEW_BASE_INTEREST_RATE);
        bankParams.setInterestRateFactor(NEW_INTEREST_RATE_FACTOR);
        bankParams.setMaxLoanAttempts(NEW_MAX_LOAN_ATTEMPTS);
        bankParams.setRiskTimeStart(NEW_RISK_TIME_START);
        bankParams.setRiskTimeEnd(NEW_RISK_TIME_END);
        bankParams.setLoanExtensionTerm(NEW_LOAN_EXTENSION_TERM);
        return bankParams;
    }
}
