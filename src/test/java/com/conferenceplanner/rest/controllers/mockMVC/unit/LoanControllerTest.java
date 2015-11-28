package com.conferenceplanner.rest.controllers.mockMVC.unit;

import com.conferenceplanner.core.communications.loans.CreateLoanQuery;
import com.conferenceplanner.core.communications.loans.CreateLoanResponse;
import com.conferenceplanner.core.communications.loans.domain.LoanRequestDetails;
import com.conferenceplanner.core.communications.loans.factories.LoanRequestDetailsFactory;
import com.conferenceplanner.core.services.common.Message;
import com.conferenceplanner.core.services.QueryExecutor;
import com.conferenceplanner.rest.controllers.LoanController;
import com.conferenceplanner.rest.domain.LoanRequest;
import com.conferenceplanner.rest.validators.LoanRequestValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.mockito.Mockito.*;

import static com.conferenceplanner.rest.domain.JsonDataFixture.*;


/**
 * Created by Ann on 16/09/14.
 */
public class LoanControllerTest
{
    MockMvc mockMvc;

    @InjectMocks
    LoanController loanController;
    @Mock
    LoanRequestValidator loanRequestValidator;
    @Mock
    QueryExecutor queryExecutor;
    @Mock
    LoanRequestDetailsFactory loanRequestDetailsFactory;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = standaloneSetup(loanController)
              .setMessageConverters(new MappingJackson2HttpMessageConverter())
              .build();

    }

    @Test
    public void testThatCreateLoanUsesHttpCreatedOnSuccess() throws Exception
    {
        when(loanRequestValidator.validate(any(LoanRequest.class)))
               .thenReturn(true);
        when(loanRequestDetailsFactory.getNewLoanRequestDetails(any(LoanRequest.class)))
                .thenReturn(new LoanRequestDetails());
        when(queryExecutor.execute(any(CreateLoanQuery.class)))
                .thenReturn(new CreateLoanResponse(true, Message.LOAN_OBTAINED_MESSAGE));

        this.mockMvc.perform(
              post("/loans")
                .content(standardLoanRequestJSON())
                .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isCreated());
    }

    @Test
    public void testThatCreateLoanUsesHttpInternalServerErrorOnFailureToGetLoan() throws Exception
    {
        when(loanRequestValidator.validate(any(LoanRequest.class)))
                .thenReturn(true);
        when(loanRequestDetailsFactory.getNewLoanRequestDetails(any(LoanRequest.class)))
                .thenReturn(new LoanRequestDetails());
        when(queryExecutor.execute(any(CreateLoanQuery.class)))
                .thenReturn(new CreateLoanResponse(false, Message.LOAN_ERROR_MESSAGE));

        this.mockMvc.perform(
                post("/loans")
                        .content(standardLoanRequestJSON())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void testThatCreateLoanUsesHttpBadRequestOnFailureToValidate() throws Exception
    {
        when(loanRequestValidator.validate(any(LoanRequest.class)))
                .thenReturn(false);
        this.mockMvc.perform(
                post("/loans")
                        .content(standardLoanRequestJSON())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
