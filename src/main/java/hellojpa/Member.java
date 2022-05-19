package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String userName;

//    @OneToOne
//    @JoinColumn(name = "LOCKER_ID")
//    private Locker locker;

    // 실무 -> 지연 로딩만 사용 해라 (join table 조회하냐(즉시로딩 EAGER), 안하냐(지연로딩 LAZY))
    //@ManyToOne(fetch = FetchType.LAZY) // (지연 로딩) proxy로 조회 (member 만 조회할때 team은 proxy로 조회한다, 이후 member.getTeam.getXXX (살제 team 을 사용하는 시점에 team 초기화(DB 조회))
    @ManyToOne(fetch = FetchType.LAZY) // (즉시 로딩)
    @JoinColumn
    private Team team;

//    @OneToMany(mappedBy = "member") // 거의 안씀
//    private List<MemberProduct> memberProducts = new ArrayList<>();

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

//    public Long getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(Long teamId) {
//        this.teamId = teamId;
//    }


//    public Team getTeam() {
//        return team;
//    }
//
//    public void changeTeam(Team team) {
//        this.team = team;
//        team.getMembers().add(this); // == team.getMembers().add(member);
//        // 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정
//        // 연관관계 편의 메소드 생성
//    }


//    public Locker getLocker() {
//        return locker;
//    }
//
//    public void setLocker(Locker locker) {
//        this.locker = locker;
//    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
