package com.conferenceplanner.core.repositories;

import com.conferenceplanner.core.domain.ConferenceRoom;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConferenceRoomRepository extends SessionProvider {

    public void create(ConferenceRoom conferenceRoom) {
        getCurrentSession().saveOrUpdate(conferenceRoom);
    }

    public ConferenceRoom getById(Integer id) {
        Session session = getCurrentSession();
        return (ConferenceRoom) session.get(ConferenceRoom.class, id);
    }

    public List<ConferenceRoom> getAll(){
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(ConferenceRoom.class);
        return (List<ConferenceRoom>)criteria.list();
    }
}
