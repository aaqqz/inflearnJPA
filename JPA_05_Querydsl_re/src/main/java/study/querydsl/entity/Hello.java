package study.querydsl.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// querydsl test entity
@Entity
@Getter @Setter
public class Hello {

    @Id
    @GeneratedValue
    private Long id;
}
