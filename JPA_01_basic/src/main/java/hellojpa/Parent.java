package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    //cascade 소유자가 하나일때 사용 권장, child를 여러군데에서 관리할때 권장 X (참조하는 곳이 하나일때 사용)
    //orphanRemoval 컬랙션에서 제거되면 data delete (참조하는 곳이 하나일때 사용)(특정 엔티티가 개인 소유할때)
    //(orphanRemoval = true) == (cascade = CascadeType.ALL || cascade = CascadeType.REMOVE) casecade all,remove 속성과 같다
    // cascade orphanRemoval 모두 적용 시켰을때 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있음
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true) // parent persist 시점에 childList 의 child 도 같이 persist
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }

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

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }
}
