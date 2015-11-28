package com.conferenceplanner.rest.controllers.mockMVC.unit;

import com.conferenceplanner.core.communications.loans.CreateLoanExtensionQuery;
import com.conferenceplanner.core.communications.loans.CreateLoanExtensionResponse;
import com.conferenceplanner.core.services.QueryExecutor;
import com.conferenceplanner.rest.controllers.LoanExtensionController;
import org.junit.Before;
import org.junit.Test;
import com.conferenceplanner.core.services.common.Message;
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


/**
 * Created by Ann on 16/09/14.
 */
public class LoanExtensionControllerTest
{
    MockMvc mockMvc;

    @InjectMocks
    LoanExtensionController loanExtensionController;

    @Mock
    QueryExecutor queryExecutor;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = standaloneSetup(loanExtensionController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void testThatCreateLoanExtensionUsesHttpCreated() throws Exception
    {
        when(queryExecutor.execute(any(CreateLoanExtensionQuery.class)))
                .thenReturn(new CreateLoanExtensionResponse(true,Message.LOAN_EXTENSION_OBTAINED_MESSAGE));
        this.mockMvc.perform(
                  post("/loans/1/extensions")
                  .contentType(MediaType.APPLICATION_JSON))
                  .andExpect(status().isCreated());
    }

    @Test
    public void testThatCreateLoanExtensionUsesHttpBadRequest_1() throws Exception
    {
        this.mockMvc.perform(
                post("/loans/null/extensions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testThatCreateLoanExtensionUsesHttpBadRequest_2() throws Exception
    {
        this.mockMvc.perform(
                post("/loans/-1/extensions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
