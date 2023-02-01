package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    // todo 테스트 검증 코드 assertj 로 변경

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(em); // querydsl 의존성 주입

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    void startJPQL() {
        // member1을 찾아라
        String qlString =
                "select m from Member m " +
                        "where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertEquals("member1", findMember.getUsername());
    }

    @Test
    void startQuerydsl() {
        // querydsl 은 jpql 의 빌더역활을 한다
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em); // querydsl 의존성 주입
//        QMember m = new QMember("m"); // 별칭 직접 지정
//        QMember member = QMember.member; // 기본 인스턴스 사용

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertEquals("member1", findMember.getUsername());
    }

    @Test
    @DisplayName("검색조건 쿼리")
    void search() {
        // 검색 조건은 .and() , . or() 를 메서드 체인으로 연결할 수 있다.

        Member findMember = queryFactory
                .selectFrom(member) // .select(member).from(member) 합치기 가능
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertEquals("member1", findMember.getUsername());

//        JPQL이 제공하는 모든 검색 조건 제공
//        member.username.eq("member1") // username = 'member1'
//        member.username.ne("member1") //username != 'member1'
//        member.username.eq("member1").not() // username != 'member1'

//        member.username.isNotNull() //이름이 is not null

//        member.age.in(10, 20) // age in (10,20)
//        member.age.notIn(10, 20) // age not in (10, 20)
//        member.age.between(10,30) //between 10, 30

//        member.age.goe(30) // age >= 30
//        member.age.gt(30) // age > 30
//        member.age.loe(30) // age <= 30
//        member.age.lt(30) // age < 30

//        member.username.like("member%") //like 검색
//        member.username.contains("member") // like ‘%member%’ 검색
//        member.username.startsWith("member") //like ‘member%’ 검색
//        ...
    }

    @Test
    @DisplayName("검색조건 쿼리(where 절 .and() 생략)")
    void searchAndParam() {
//        where() 에 파라미터로 검색조건을 추가하면 AND 조건이 추가됨
//        이 경우 null 값은 무시 메서드 추출을 활용해서 동적 쿼리를 깔끔하게 만들 수 있음
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        (member.age.eq(10)),
                        null
                )
                .fetchOne();

        assertEquals("member1", findMember.getUsername());
    }

    @Test
    @DisplayName("결과 조회")
    void resultFetch() {
//        fetch() : 리스트 조회, 데이터 없으면 빈 리스트 반환
//        fetchOne() : 단 건 조회
//          - 결과가 없으면 : null
//          - 결과가 둘 이상이면 : com.querydsl.core.NonUniqueResultException
//        fetchFirst() : limit(1).fetchOne()

//        -- deprecated --
//        fetchResults() : 페이징 정보 포함, total count 쿼리 추가 실행
//        fetchCount() : count 쿼리로 변경해서 count 수 조회

        // List
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        // 단 건
        Member fetchOne = queryFactory
                .selectFrom(member)
                .fetchOne();

        // 처음 한 건 조회
        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst(); // == limit(1).fetchOne()

        // 페이징에서 사용 .fetchResults()
        // count 쿼리로 사용 .fetchCount()
    }

    /**
     * 회원 정렬 순서
     * 1, 회원 나이 내림차순(desc)
     * 2, 회원 이름 올림차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    @DisplayName("정렬")
    void sort() {
//        desc() , asc() : 일반 정렬
//        nullsLast() , nullsFirst() : null 데이터 순서 부여

        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertEquals("member5", member5.getUsername());
        assertEquals("member6", member6.getUsername());
        assertNull(memberNull.getUsername());
    }

    @Test
    @DisplayName("페이징")
    void paging1() {
//        limit : 페이지당 갯수
//        offset : 데이터를 가져오기 시작할 위치 => (페이지번호 - 1 ) * 페이지당갯수
//        ex.
//        limit 10 offset 0 : 1~10까지 반환
//        limit 10 offset 10 : 11~20까지 반환

        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) //0부터 시작(zero index
                .limit(2) //최대 2건 조회
                .fetch();

        assertEquals(2, result.size());
    }

    /**
     * JPQL
     * select
     * COUNT(m), //회원수
     * SUM(m.age), //나이 합
     * AVG(m.age), //평균 나이
     * MAX(m.age), //최대 나이
     * MIN(m.age) //최소 나이
     * from Member m
     */
    @Test
    @DisplayName("집합")
    void aggregation() {
        // Tuple => select 절 custom시 Tuple 반환 (프로젝션과 결과반환 에서 설명)
        // Tuple 보다 프로젝션(DTO 조회)
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        // tuple 사용시 tuple.get( select 절에서 사용한 메소드 )
        Tuple tuple = result.get(0);
        Long count = tuple.get(member.count());

        assertEquals(4, tuple.get(member.count()));
        assertEquals(100, tuple.get(member.age.sum()));
        assertEquals(25, tuple.get(member.age.avg()));
        assertEquals(40, tuple.get(member.age.max()));
        assertEquals(10, tuple.get(member.age.min()));
    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라.
     */
    @Test
    @DisplayName("집합(groupBy)")
    void group() {
//        groupBy , 그룹화된 결과를 제한하려면 having
//        .groupBy(item.price)
//        .having(item.price.gt(1000))

        List<Tuple> result = queryFactory
                .select(
                        team.name,
                        member.age.avg()
                )
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);
        assertEquals("teamA", teamA.get(team.name));
        assertEquals(15, teamA.get(member.age.avg()));

        assertEquals("teamB", teamB.get(team.name));
        assertEquals(35, teamB.get(member.age.avg()));
    }


}
