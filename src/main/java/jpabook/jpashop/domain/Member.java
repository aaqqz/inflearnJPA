package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    //@NotEmpty // javax.validation api 에서 param을 Entity로 받으면 안됨 DTO로 생성 하자
    private String name;

    @Embedded
    private Address address;

    //@JsonIgnore // 회원 조회시 order 반환하지 않기 위해 -> Entity를 반환시 Entity에 화면을 위한 로직이 계속 추가됨 -> 이러한 이유로 Entity를 반환하면 안됨
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


}
