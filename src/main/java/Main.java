import configuration.SpringConfig;
import model.jpa.Book;
import model.jpa.BookInfo;
import model.jpa.Person;
import model.jpa.Student;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.JPAService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        JPAService jpaService = context.getBean(JPAService.class);
       // jpaService.savePersonIntoDB();
        //Person janusz = jpaService.getPersonFromDB("Janusz");
        //Person janusz = jpaService.getPersonFromDbUsingNativeSQL("Janusz");
    /*    Person janusz = jpaService.getPersonFromDbUsingJpgl("Janusz");
        System.out.println(janusz);

        jpaService.modifyPerson("Janusz");

        Person janusz2 = jpaService.getPersonFromDbUsingJpgl("Janusz");

        System.out.println(janusz2);
*/

        List<Student> students = jpaService.prepareStudentData();

        jpaService.addStudentIntoDB(students);

       List<Book> books = jpaService.getBooksTakenByStudentsNativeQuery("Krakow");

        System.out.println(books);

        List<Book> books1 = jpaService.getBooksTakenByStudentsJpql("Krakow");

        System.out.println(books1);

        List<BookInfo> bookInfo = jpaService.getBookInfoTakenByStudentsJpql("Krakow");
        System.out.println(bookInfo);

        List<BookInfo> bookInfo2 = jpaService.getBooksInfoProjectionWithCrtiteriaApi("Krakow");
        System.out.println(bookInfo2);
    }

}
