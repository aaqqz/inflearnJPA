package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
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

//        EntityManager 로 JPAQueryFactory 생성
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em); // querydsl 의존성 주입

//        Querydsl은 JPQL 빌더

//        JPQL: 문자(실행 시점 오류), Querydsl: 코드(컴파일 시점 오류)
//        JPQL: 파라미터 바인딩 직접, Querydsl: 파라미터 바인딩 자동 처리

//        QMember m = new QMember("m"); // 별칭 직접 지정
//        QMember member = QMember.member; // 기본 인스턴스 사용

//        참고: 같은 테이블을 조인해야 하는 경우가 아니면 기본 인스턴스를 사용하자

        // member1을 찾아라
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1")) //파라미터 바인딩 처리
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName("검색조건_(기본 검색 쿼리)")
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
    @DisplayName("검색조건_(AND 조건을 파라미터로 처리, .and() 생략)")
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
    @DisplayName("결과조회")
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
                .offset(1) //0부터 시작(zero index)
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
    @DisplayName("집합_(집합함수)")
    void aggregation() {
        // Tuple => select 절 custom시 Tuple 반환 (프로젝션과 결과반환 에서 설명)
        // Tuple 보다 프로젝션(DTO 조회 사용 권장)
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
//        JPQL의 on 과 성능 최적화를 위한 fetch 조인 제공 다음 on 절에서 설명

//        조인의 기본 문법은 첫 번째 파라미터에 조인 대상을 지정하고, 두 번째 파라미터에 별칭(alias)으로 사용할 Q 타입을 지정하면 된다.
//        join(조인 대상, 별칭으로 사용할 Q타입)

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
     * 세타 조인(연관관계가 없는 필드로 조인
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
    @DisplayName("조인 on절_(조인 대상 필터링)")
    void join_on_filtering() {
//        참고: on 절을 활용해 조인 대상을 필터링 할 때, 외부조인이 아니라 내부조인(inner join)을 사용하면,
//        where 절에서 필터링 하는 것과 기능이 동일하다. 따라서 on 절을 활용한 조인 대상 필터링을 사용할 때,
//        내부조인 이면 익숙한 where 절로 해결하고, 정말 외부조인이 필요한 경우에만 이 기능을 사용하자.

//        조인의 기본 문법은 첫 번째 파라미터에 조인 대상을 지정하고, 두 번째 파라미터에 별칭(alias)으로 사용할 Q 타입을 지정하면 된다.
//        join(조인 대상, 별칭으로 사용할 Q타입)

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
    @DisplayName("조인 on절_(연관관계 없는 엔티티 외부 조인)")
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
    @DisplayName("조인 페치조인_(페치 조인 미적용)")
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
    @DisplayName("조인 페치조인_(페치 조인 적용)")
    void fetchJoinUse() {
//        페치 조인은 SQL에서 제공하는 기능은 아니다. SQL조인을 활용해서 연관된 엔티티를 SQL 한번에
//        조회하는 기능이다. 주로 성능 최적화에 사용하는 방법이다.
//        ## 페치 조인 적용 ##
//        즉시로딩으로 Member, Team SQL 쿼리 조인으로 한번에 조회

//        join(), leftJoin() 등 조인 기능 뒤에 fetchJoin() 이라고 추가하면 된다.
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
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
//                        JPAExpressions.select()
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

    /**
     * Case 문
     * select, 조건절(where), order by에서 사용 가능
     * 가능한 쿼리는 조회만 하고 application 단에서 처리 하는것도 좋은 방식
     */
    @Test
    @DisplayName("Case_(단순한 조건)")
    void basicCase() {
        List<String> fetch = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : fetch) {
            System.out.println("s = " + s);
        }
    }

    @Test
    @DisplayName("Case_(복잡한 조건)")
    void complexCase() {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타")
                ).from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    /**
     * 1. 0 ~ 30살이 아닌 회원을 가장 먼저 출력
     * 2. 0 ~ 20살 회원 출력
     * 3. 21 ~ 30살 회원 출력
     */
    @Test
    @DisplayName("Case_(orderBy 에서 Case 활용)")
    void orderByCase() {
//        Querydsl은 자바 코드로 작성하기 때문에 rankPath 처럼 복잡한 조건을 변수로 선언해서 select 절,
//        orderBy 절에서 함께 사용할 수 있다.
        NumberExpression<Integer> rankPath = new CaseBuilder()
                .when(member.age.between(0, 20)).then(2)
                .when(member.age.between(21, 30)).then(1)
                .otherwise(3);

        List<Tuple> result = queryFactory
                .select(member.username, member.age, rankPath)
                .from(member)
                .orderBy(rankPath.desc())
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            Integer rank = tuple.get(rankPath);
            System.out.println("username = " + username + " age = " + age + " rank = " + rank);
        }
    }

    @Test
    @DisplayName("distinct")
    void distinct() {
//        distinct는 JPQL의 distinct와 같다
        Member memberA = new Member("member99");
        Member memberB = new Member("member99");
        Member memberC = new Member("member99");
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);

        List<String> result = queryFactory
                .select(member.username).distinct()
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    @DisplayName("상수 가져오기")
    void constant() {
//        상수가 필요하면 Expressions.constant(xxx) 사용 (상수 : 항상 일정한 값)
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    @DisplayName("문자 더하기")
    void concat() {
//        member.age.stringValue() 부분이 중요한데, 문자가 아닌 다른 타입들은 stringValue () 로
//        문자로 변환할 수 있다.이 방법은 ENUM을 처리할 때도 자주 사용한다.
        //{username}_{age}
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    /**
     * 프로젝션 : select 대상 지정
     * 프로젝션 대상이 하나면 타입을 명확하게 지정할 수 있음
     * 프로젝션 대상이 둘 이상이면 튜플이나 DTO로 조회
     */
    @Test
    @DisplayName("프로젝션 결과 반환 기본_(프로젝션 대상이 하나)")
    void simpleProjection() {
        List<String> result1 = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : result1) {
            System.out.println("s1 = " + s);
        }

        List<Member> result2 = queryFactory
                .select(member)
                .from(member)
                .fetch();

        for (Member m : result2) {
            System.out.println("s2 = " + m);
        }
    }

    @Test
    @DisplayName("프로젝션 결과 반환 기본_(튜플 조회)")
    void tupleProjection() {
//        프로젝션 대상이 둘 이상일 때 사용

//        Tuple 객체 사용시 Repository 계층에서 사용하는건 괜찮으나,
//        Controller, Service 계층에서 사용하는건 지양

        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    @Test
    @DisplayName("프로젝션 결과 반환 DTO 조회_(순수 JPA 에서 DTO 조회)")
    void findDtoByJPQL() {
//        순수 JPA에서 DTO를 조회할 때는 new 명령어를 사용해야함
//        DTO의 package이름을 다 적어줘야해서 지저분함
//        생성자 방식만 지원함
        List<MemberDto> result = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     * Querydsl 빈 생성(Bean population)
     * 결과를 DTO 반환할 때 사용
     * 다음 3가지 방법 지원
     * 프로퍼티 접근
     * 필드 직접 접근
     * 생성자 사용
     */
    @Test
    @DisplayName("프로젝션 결과 반환 DTO 조회_(querydsl 프로퍼티 접근 Setter)")
    void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("프로젝션 결과 반환 DTO 조회_(querydsl 필드 직접 접근)")
    void findDtoByField() {
//        getter, setter 이 필요없다
//        field 에 값을 할당한다
//            private String username;
//            private int age;

        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("프로젝션 결과 반환 DTO 조회_(querydsl 생성자 사용)")
    void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("프로젝션 결과 반환 DTO 조회_(querydsl 별칭이 다를 때)")
    void findUserDto() {
//        프로퍼티나, 필드 접근 생성 방식에서 이름이 다를 때 해결 방안
//        ExpressionUtils.as(source,alias) : 필드나, 서브 쿼리에 별칭 적용
//        username.as("memberName") : 필드에 별칭 적용
        List<UserDto> result1 = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        member.age))
                .from(member)
                .fetch();

        for (UserDto userDto : result1) {
            System.out.println("userDto = " + userDto);
        }

        QMember memberSub = new QMember("memberSub");
        List<UserDto> result2 = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub), "age")
                ))
                .from(member)
                .fetch();

        for (UserDto userDto : result2) {
            System.out.println("userDto = " + userDto);
        }
    }

    @Test
    @DisplayName("프로젝션 결과 반환_(생성자 + @QueryProjection)")
    void findDyoQueryProjection() {
//        생성자에 @QueryProjection 어노테이션 추가
//        ./gradlew compileQuerydsl
//        QMemberDto 생성 확인

//        이 방법은 컴파일러로 타입을 체크할 수 있으므로 가장 안전한 방법이다. 다만 DTO에 QueryDSL
//        어노테이션을 유지해야 하는 점과 DTO까지 Q 파일을 생성해야 하는 단점이 있다.

        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     * 동적 쿼리 - BooleanBuilder 사용
     * 동적 쿼리를 해결하는 두가지 방식
     * BooleanBuilder
     * Where 다중 파라미터 사용 (실무에서 자주 사용)
     */
    @Test
    @DisplayName("동적 쿼리_(BooleanBuilder 사용)")
    void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        BooleanBuilder builder = new BooleanBuilder();
        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }

        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    /**
     * 실무에서 자주 사용
     */
    @Test
    @DisplayName("동적 쿼리_Where 다중 파라미터 사용)")
    void dynamicQuery_whereParam() {
        // 실무에서 자주 사용
//        where 조건에 null 값은 무시된다.
//        메서드를 다른 쿼리에서도 재활용 할 수 있다.
//        쿼리 자체의 가독성이 높아진다.
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCond), ageEq(ageCond))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    // 조합 가능
    // null 체크는 주의해서 처리해야함
    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    @Test
    void bulkUpdate() {
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();
    }

}
