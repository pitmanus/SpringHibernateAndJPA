package model.jpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Computer extends Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "computer_student",
            joinColumns = {@JoinColumn(name = "computer_id", referencedColumnName = "id")},
            inverseJoinColumns =  {@JoinColumn(name = "student_id", referencedColumnName = "id")}
    )
    private List<Student> students = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<Student> getStudens() {
        return students;
    }

    public void setStudens(List<Student> studens) {
        this.students = studens;
    }
}
