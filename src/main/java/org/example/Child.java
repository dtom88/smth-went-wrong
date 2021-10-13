package org.example;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Child {
    @Id
    public Long id;
    public State state;
    public Long parentId;
}
