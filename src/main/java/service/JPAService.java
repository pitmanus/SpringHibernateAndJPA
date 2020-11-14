package service;

import configuration.JpaConfig;
import model.jpa.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;

@Service
public class JPAService {
    private EntityManager entityManager;

    public JPAService(JpaConfig jpaConfig) {
        entityManager = jpaConfig.getEntityManager();
    }

    public void savePersonIntoDB(){
        entityManager.getTransaction().begin();
        entityManager.persist(preparePerson());
        entityManager.getTransaction(). commit();
    }

    private Person preparePerson(){

        Address address = new Address();
        address.setCity("Warszawa");
        address.setStreet("Wiejska");
        address.setZipCode("00-100");

        Person p = new Person();
        p.setName("Janusz");
        p.setSurname("Nosacz");
        p.setAddress(address);

        return p;
    }

    public Person getPersonFromDB(String name){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = cb.createQuery(Person.class);
        Root<Person> fromPerson = query.from(Person.class);

        query.select(fromPerson).where(cb.equal(fromPerson.get(Person_.name), name));

        return entityManager.createQuery(query).getSingleResult();

        // select p from Person p where p.name = :name
    }

    public Person getPersonFromDbUsingNativeSQL(String name){
        Query q = entityManager
                .createNativeQuery("select * from person p where p.name =?1", Person.class);
        q.setParameter(1, name);
        return (Person) q.getSingleResult();
    }

    public Person getPersonFromDbUsingJpgl(String name){
        TypedQuery<Person> q = entityManager.createQuery("select p from Person p where p.name = :personName", Person.class);
                q.setParameter("personName", name);

                return q.getSingleResult();
    }

    public void modifyPerson(String name){
        Person personFromDB = getPersonFromDB(name);
        Person myPerson = new Person();

        Address address = new Address();
        address.setCity(personFromDB.getAddress().getCity());
        address.setZipCode(personFromDB.getAddress().getZipCode());
        address.setStreet(personFromDB.getAddress().getStreet());
        myPerson.setAddress(address);
        myPerson.setSurname(personFromDB.getSurname());
        myPerson.setName(personFromDB.getName());
        myPerson.setId(personFromDB.getId());

        entityManager.getTransaction().begin();
        myPerson.setSurname("Nosacz5");
        Person merge = entityManager.merge(myPerson);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        merge.setSurname("Nosacz8");
        entityManager.getTransaction().commit();
    }

    public void addStudentIntoDB(List<Student> students){
        entityManager.getTransaction().begin();
        students.forEach(student -> entityManager.persist(student));
        entityManager.getTransaction().commit();
    }

    public List<Book> getBooksTakenByStudentsNativeQuery(String location){
        Query nativeQuery = entityManager.createNativeQuery(
            "Select distinct b.* from book b " +
                    "join student s on b.student_id = s.id " +
                    "join computer_student cs on s.id = cs.student_id " +
                    "join computer c on c.id = cs.computer_id " +
                    "where c.localization = ?1", Book.class);
        nativeQuery.setParameter(1, location);
        return nativeQuery.getResultList();
    }

    public List<Book> getBooksTakenByStudentsJpql(String location){
        TypedQuery<Book> query = entityManager.createQuery(
                "SELECT b FROM Book b "
                + "JOIN b.student s "
                + "JOIN s.computers c "
                + "WHERE c.localization = :location", Book.class
        );
        query.setParameter("location", location);
        return query.getResultList();
    }

    public List<BookInfo> getBookInfoTakenByStudentsJpql(String location){
        TypedQuery<BookInfo> query = entityManager.createQuery(
                "SELECT "
                        + "new model.jpa.BookInfo(b.title, s.name, s.surname) "
                        + "FROM Book b "
                        + "JOIN b.student s "
                        + "JOIN s.computers c "
                        + "WHERE c.localization = :location", BookInfo.class
        );
        query.setParameter("location", location);
        return query.getResultList();
    }

    public List<BookInfo> getBooksInfoProjectionWithCrtiteriaApi(String location){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookInfo> query = cb.createQuery(BookInfo.class);
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Student> student = bookRoot.join("student");
        Join<Student, Computer> computers = student.join("computers");

        //Define DTO
        query.select(cb.construct(
                BookInfo.class,
                bookRoot.get("title"),
                student.get("name"),
                student.get("surname")))
                .where(cb.equal(computers.get("localization"), location));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Student> prepareStudentData() {
        Computer computer1 = new Computer();
        computer1.setSerialNumber("SERIAL1");
        computer1.setDeviceName("ASDASD");
        computer1.setLocalization("Krakow");

        Computer computer2 = new Computer();
        computer2.setSerialNumber("SERIAL2");
        computer2.setDeviceName("ASDASD2");
        computer2.setLocalization("Warszawa");

        Author author = new Author();
        author.setName("Julian");
        author.setSurname("Tuwim");

        Author author2 = new Author();
        author2.setName("Jan");
        author2.setSurname("Brzechwa");

        Author author3 = new Author();
        author3.setName("Aleksander");
        author3.setSurname("Kwaśniewski");

        Book book1 = new Book();
        book1.setTitle("Wiersze");
        book1.setAuthor(author);

        Book book2 = new Book();
        book2.setTitle("Akademia Pana Kleksa");
        book2.setAuthor(author2);

        Book book3 = new Book();
        book3.setTitle("Akademia Pana Aleksandra");
        book3.setAuthor(author3);

        Student student = new Student();
        student.setName("Pioter");
        student.setSurname("Nosacz");
        student.getBooks().add(book1);
        student.getComputers().add(computer1);
        student.getComputers().add(computer2);
        computer1.getStudens().add(student);
        computer2.getStudens().add(student);

        Student student2 = new Student();
        student2.setName("Jan");
        student2.setSurname("Nowak");
        student2.getBooks().add(book2);
        student2.getComputers().add(computer1);
        computer1.getStudens().add(student2);

        Student student3 = new Student();
        student3.setName("MAriusz");
        student3.setSurname("Chrusciel");
        student3.getBooks().add(book3);
        student3.getComputers().add(computer2);
        computer2.getStudens().add(student3);

        book1.setStudent(student);
        book2.setStudent(student2);
        book3.setStudent(student3);

        return Arrays.asList(student, student2, student3);
    }


}
