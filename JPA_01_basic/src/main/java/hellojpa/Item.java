package hellojpa;

import javax.persistence.*;

@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 기본은 단일테이블 전략

//@Inheritance(strategy = InheritanceType.JOINED) // 기본은 단일테이블 전략, joined == join 전략
//@DiscriminatorColumn // default -> entity명 (단일테이블 전략일때 필요없음-자동세팅됨) (join 전략일때 필요)

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 구현 클래스 마다 테이블 전략 (
public abstract class Item { // 구현 클래스 마다 테이블 전략(abstract class) 나머지 (class) // 쓰지않는게 좋다

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
