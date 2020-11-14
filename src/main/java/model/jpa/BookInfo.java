package model.jpa;

public class BookInfo {

    private String title;
    private String name;
    private String surname;

    public BookInfo(String title, String name, String surname) {
        this.title = title;
        this.name = name;
        this.surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}

