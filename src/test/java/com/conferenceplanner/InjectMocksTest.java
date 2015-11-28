package com.conferenceplanner;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Created by Ann on 02/10/14.
 */
public class InjectMocksTest
{
    @Before
    public void initMocks()
    {
         MockitoAnnotations.initMocks(this);
    }
}

