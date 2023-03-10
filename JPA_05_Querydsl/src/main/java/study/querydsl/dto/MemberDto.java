package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {

    private String username;
    private int age;

    private Long test;

    @QueryProjection // DTO QFile 생성
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }

    @QueryProjection
    public MemberDto(String username, int age, Long test) {
        this.username = username;
        this.age = age;
        this.test = test;
    }
}
