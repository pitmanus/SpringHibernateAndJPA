package configuration;

import model.jpa.Author;
import model.jpa.Book;
import model.jpa.Computer;
import model.jpa.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

public class HibernateConfig {
    private static SessionFactory sessionFactory = sessionFactory();

            public static SessionFactory sessionFactory(){
        if (sessionFactory!=null){
            return sessionFactory;
        }
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
        registryBuilder.applySettings(readProperties());
                StandardServiceRegistry serviceRegistry = registryBuilder.build();

                Configuration cfg = new org.hibernate.cfg.Configuration();
                cfg.addAnnotatedClass(Student.class);
                cfg.addAnnotatedClass(Book.class);
                cfg.addAnnotatedClass(Computer.class);
                cfg.addAnnotatedClass(Author.class);

                sessionFactory = cfg.buildSessionFactory(serviceRegistry);

                return sessionFactory;

            }

            private static Properties readProperties(){
                Properties properties = new Properties();
                try {
                    properties.load(HibernateConfig.class.getResourceAsStream("/dao.properties"));
                    return properties;
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                return properties;
            }
}

