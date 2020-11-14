package service;

import configuration.HibernateConfig;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

@Service
public class HibernateService {

    private SessionFactory sessionFactory;

    public HibernateService() {
        this.sessionFactory = HibernateConfig.sessionFactory();
    }
}
