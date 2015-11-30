package com.conferenceplanner.core.repositories;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class ConferenceRepository extends SessionProvider {

    public void create(Conference conference) {
        getCurrentSession().saveOrUpdate(conference);
    }

    public void update(Conference conference) {
        getCurrentSession().saveOrUpdate(conference);
    }

    public Conference getById(Integer id) {
        Session session = getCurrentSession();
        return (Conference) session.get(Conference.class, id);
    }

    public List<Conference> getAllAvailable(){
        LocalDateTime now = LocalDateTime.now();
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(Conference.class);
        criteria.add(Restrictions.gt("startDateTime", now));
        criteria.add(Restrictions.eq("cancelled", false));
        return (List<Conference>)criteria.list();
    }

    public List<Conference> getAvailableByConferenceRoom(ConferenceRoom conferenceRoom) {
        LocalDateTime now = LocalDateTime.now();
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(Conference.class);
        criteria.add(Restrictions.eq("conferenceRoom", conferenceRoom));
        criteria.add(Restrictions.gt("startDateTime", now));
        criteria.add(Restrictions.eq("cancelled", false));
        return (List<Conference>)criteria.list();
    }
}
