package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static javax.persistence.CascadeType.*;

@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String userName;

    @Embedded
    private Address homeAddress;

    @ElementCollection // default 지연로딩, 쓰면 안됨
    @CollectionTable(name = "FAVORITE_FOOD",
            joinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFood = new HashSet<>();

    //    @ElementCollection // 쓰면 안됨
//    @CollectionTable(name = "ADDRESS",
//            joinColumns = @JoinColumn(name = "MEMBER_ID")
//    )
//    private List<Address> addressHistory = new ArrayList<>();
    @OneToMany(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();


//    //기간 Period
//    @Embedded
//    private Period workPeriod;
//
//    //주소 Address
//    @Embedded
//    private Address homeAddress;

    //주소 Address
//    @Embedded //
//    @AttributeOverrides({
//            @AttributeOverride(name = "city",
//                    column = @Column(name = "WORK_CITY")),
//            @AttributeOverride(name = "street",
//                    column = @Column(name = "WORK_STREET")),
//            @AttributeOverride(name = "zipcode",
//                    column = @Column(name = "WORK_ZIPCODE"))
//    })
//    private Address workAddress; // 중복 가능

    // 실무 -> 지연 로딩만 사용 해라 (join table 조회하냐(즉시로딩 EAGER), 안하냐(지연로딩 LAZY))
    //@ManyToOne(fetch = FetchType.LAZY) // (지연 로딩) proxy로 조회 (member 만 조회할때 team은 proxy로 조회한다, 이후 member.getTeam.getXXX (살제 team 을 사용하는 시점에 team 초기화(DB 조회))
//    @ManyToOne(fetch = FetchType.LAZY) // (즉시 로딩)
//    @JoinColumn
//    private Team team;

//    @OneToMany(mappedBy = "member") // 거의 안씀
//    private List<MemberProduct> memberProducts = new ArrayList<>();

//    public void changeTeam(Team team) {
//        this.team = team;
//        team.getMembers().add(this); // == team.getMembers().add(member);
//        // 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정
//        // 연관관계 편의 메소드 생성
//    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(Set<String> favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

//    public List<Address> getAddressHistory() {
//        return addressHistory;
//    }
//
//    public void setAddressHistory(List<Address> addressHistory) {
//        this.addressHistory = addressHistory;
//    }


    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }
}
