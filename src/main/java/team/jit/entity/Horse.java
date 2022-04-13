package team.jit.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Horse {

    @Id
    private Long id;

    private String name;


    public Horse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Horse() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
