package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Locker {

    @Id
    @GeneratedValue
//    @Column(name = "LOCKER_ID")
    private Long Id;

    private String name;

    @OneToOne(mappedBy = "locker")
    private Member member;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
