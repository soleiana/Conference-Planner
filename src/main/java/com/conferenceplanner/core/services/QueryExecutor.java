package com.conferenceplanner.core.services;

import com.conferenceplanner.core.communications.DomainQuery;
import com.conferenceplanner.core.communications.DomainResponse;

/**
 * Created by Ann on 01/10/14.
 */
public interface QueryExecutor
{
    <T extends DomainResponse> T execute(DomainQuery<T> query);
}
