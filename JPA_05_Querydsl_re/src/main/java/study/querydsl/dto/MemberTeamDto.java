package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberTeamDto {

    private Long MemberId;
    private String username;
    private int age;
    private Long teamId;
    private String teamName;

//    @QueryProjection 을 사용하면 해당 DTO가 Querydsl을 의존하게 된다. 이런 의존이 싫으면,
//    해당 에노테이션을 제거하고, Projection.bean(), fields(), constructor() 을 사용하면 된다
    @QueryProjection // 단점 dto 가 querydsl 에 의존, 대응방법( 프로퍼티 접근, 필드 직접 접근, 생성자 사용 )
    public MemberTeamDto(Long memberId, String username, int age, Long teamId, String teamName) {
        MemberId = memberId;
        this.username = username;
        this.age = age;
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
