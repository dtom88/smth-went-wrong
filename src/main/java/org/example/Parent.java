package org.example;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Parent {

    @Id
    private Long id;

    private State state;

    @OneToMany
    private List<Child> children;
}
