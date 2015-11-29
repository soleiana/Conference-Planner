package com.conferenceplanner.core.repositories;


import com.conferenceplanner.core.domain.Participant;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParticipantRepository extends SessionProvider {

    public void create(Participant participant) {
        getCurrentSession().saveOrUpdate(participant);
    }

    public Participant getById(Integer id) {
        Session session = getCurrentSession();
        return (Participant) session.get(Participant.class, id);
    }

    public List<Participant> getAll() {
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(Participant.class);
        return (List<Participant>)criteria.list();
    }
}
