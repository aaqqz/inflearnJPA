package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // jpa 표준 스팩 -> entity 는 기본 생성자 하나가 필요 (access level 이 protected 까지)
@ToString(of = {"id", "username", "age"}) // 연관관계 필드는 제외 -> 무한루프를 돈다
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // 어노테이션으로 해결
    //protected Member() {} // jpa 표준 스팩 -> entity 는 기본 생성자 하나가 필요 (access level 이 protected 까지)

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    // 연관관계 메서드 Member && Team 같이 수정
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
