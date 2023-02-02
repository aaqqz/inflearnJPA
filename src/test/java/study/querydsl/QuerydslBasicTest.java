package study.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

import javax.persistence.*;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

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

        assertThat(findMember.getUsername()).isEqualTo("member1");
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
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName("검색조건")
    void search() {
        // 검색 조건은 .and() , . or() 를 메서드 체인으로 연결할 수 있다.

        Member findMember = queryFactory
                .selectFrom(member) // .select(member).from(member) 합치기 가능
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

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
    @DisplayName("검색조건_(where 절 .and() 생략)")
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
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName("결과_조회")
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
                .where(member.username.eq("member1"))
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

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
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

        assertThat(result.size()).isEqualTo(2);
        //assertEquals(2, result.size());
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

        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라.
     */
    @Test
    @DisplayName("집합_(groupBy)")
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

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    /**
     * 팀 A에 소속된 모든 회원
     */
    @Test
    @DisplayName("조인_(기본 조인)")
    void join() {
//        join() , innerJoin() : 내부 조인(inner join)
//        leftJoin() : left 외부 조인(left outer join)
//        rightJoin() : rigth 외부 조인(rigth outer join)

        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team) // on절 id 매칭O
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    /**
     *  세타 조인(연관관계가 없는 필드로 조인
     * 회원의 이름이 팀 이름과 같은 회원 조회
     */
    @Test
    @DisplayName("세타조인_(연관관계 없을때)")
    void theta_join() {
//        from 절에 여러 엔티티를 선택해서 세타 조인
//        외부 조인 불가능 -> 조인 on을 사용하면 외부 조인 가능

        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'teamA'
     * SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID=t.id and t.name='teamA'
     */
    @Test
    @DisplayName("조인_on절_(조인 대상 필터링)")
    void join_on_filtering() {
//        참고: on 절을 활용해 조인 대상을 필터링 할 때, 외부조인이 아니라 내부조인(inner join)을 사용하면,
//        where 절에서 필터링 하는 것과 기능이 동일하다. 따라서 on 절을 활용한 조인 대상 필터링을 사용할 때,
//        내부조인 이면 익숙한 where 절로 해결하고, 정말 외부조인이 필요한 경우에만 이 기능을 사용하자.

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team) // on절 id 매칭O
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 2. 연관관계 없는 엔티티 외부 조인
     * 예) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
     * JPQL: SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name
     * SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.username = t.name
     */
    @Test
    @DisplayName("조인_on절_(연관관계 없는 엔티티 외부 조인)")
    void join_on_no_relation() {
//        주의! 문법을 잘 봐야 한다. leftJoin() 부분에 일반 조인과 다르게 엔티티 하나만 들어간다.
//        일반조인: leftJoin(member.team, team)
//        on조인: from(member).leftJoin(team).on(xxx)

        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team) // on절 id 매칭X
                .on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    @DisplayName("조인_페치조인_(페치 조인 미적용)")
    void fetchJoinNo() {
//        페치 조인은 SQL에서 제공하는 기능은 아니다. SQL조인을 활용해서 연관된 엔티티를 SQL 한번에
//        조회하는 기능이다. 주로 성능 최적화에 사용하는 방법이다.
//        ## 페치 조인 미적용 ##
//        지연로딩으로 Member, Team SQL 쿼리 각각 실행
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        // Member.class
//        @ManyToOne(fetch = FetchType.LAZY)
//        @JoinColumn(name = "team_id")
//        private Team team;

//        Member class 에 team 이 LAZY 로 설정되어 있어,
//        쿼리 실행시 member 만 조회 한다.

        boolean loaded1 = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());// 엔티티 로딩 여부 반환
        assertThat(loaded1).as("패치 조인 미적용").isFalse();

        System.out.println("###");

        String name = findMember.getTeam().getName();
        System.out.println("name = " + name);
        boolean loaded2 = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());// 엔티티 로딩 여부 반환
        assertThat(loaded2).as("패치 조인 미적용").isTrue();
    }

    @Test
    @DisplayName("조인_페치조인_(페치 조인 적용)")
    void fetchJoinUse() {
//        페치 조인은 SQL에서 제공하는 기능은 아니다. SQL조인을 활용해서 연관된 엔티티를 SQL 한번에
//        조회하는 기능이다. 주로 성능 최적화에 사용하는 방법이다.
//        ## 페치 조인 적용 ##
//        즉시로딩으로 Member, Team SQL 쿼리 조인으로 한번에 조회
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin() // join(), leftJoin() 등 조인 기능 뒤에 fetchJoin() 이라고 추가하면 된다.
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();
    }

    /**
     * 나이가 가장 많은 회원 조회
     */
    @Test
    @DisplayName("서브쿼리_(서브 쿼리 eq 사용)")
    void subQueryEq() {

//        JPAExpressions static import
        QMember memberSub = new QMember("memberSub"); // 서브쿼리용 alias 생성

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    /**
     * 나이가 평균 이상인 회원
     */
    @Test
    @DisplayName("서브쿼리_(서브 쿼리 goe 사용)")
    void subQueryGoe() {

        QMember memberSub = new QMember("memberSub"); // 서브쿼리용 alias 생성

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }

    /**
     * 나이가 10 초과인 회원
     */
    @Test
    @DisplayName("서브쿼리_(서브쿼리 여러 건 처리, in 사용)")
    void subQueryIn() {

        QMember memberSub = new QMember("memberSub"); // 서브쿼리용 alias 생성

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    /**
     * 나이가 10 초과인 회원
     */
    @Test
    @DisplayName("서브쿼리_(select 절에 서브쿼리)")
    void selectSubQuery() {

        QMember memberSub = new QMember("memberSub"); // 서브쿼리용 alias 생성

        List<Tuple> result = queryFactory
                .select(member,
                        select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

//    from 절의 서브쿼리 한계
//    JPA JPQL 서브쿼리의 한계점으로 from 절의 서브쿼리(인라인 뷰)는 지원하지 않는다. 당연히 Querydsl
//    도 지원하지 않는다. 하이버네이트 구현체를 사용하면 select 절의 서브쿼리는 지원한다. Querydsl도
//    하이버네이트 구현체를 사용하면 select 절의 서브쿼리를 지원한다.
//    from 절의 서브쿼리 해결방안
//      1. 서브쿼리를 join으로 변경한다. (가능한 상황도 있고, 불가능한 상황도 있다.)
//      2. 애플리케이션에서 쿼리를 2번 분리해서 실행한다.
//      3. nativeSQL을 사용한다.

}
