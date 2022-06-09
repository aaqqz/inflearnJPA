package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 쿼리 메소드
    // 메스드명으로 쿼리 생성 (조건이 2개가 넘어가면 메소드명이 길어짐 - 다른 방법으로 푸는것을 추천)
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 전체 조회
    List<Member> findHelloBy();

    List<Member> findTop3HelloBy();

    // NamedQuery 잘 안씀
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // 실무에서 많이 씀 (컴파일 시점에 오류 체크됨)
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional


    //Page<Member> findByAge(int age, Pageable pageable); // 페이징 O
    //Slice<Member> findByAge(int age, Pageable pageable); // 페이징 X
    //List<Member> findByAge(int age, Pageable pageable); // 페이징 X

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable); // 카운트 쿼리 분리
}
