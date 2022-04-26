package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity //jpa가 관리한다
//@Table(name = "USER") //DB table 명은 class 명을 따르지만 변경하고 싶은때
public class Member {

    @Id //pk 설정
    private Long id;

    //@Column(name = "usename") //DB column 명은 변수명을 따르지만 변경하고 싶은때
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
