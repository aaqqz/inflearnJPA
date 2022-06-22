package study.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam.*;
import study.querydsl.entity.Team;


import javax.lang.model.SourceVersion;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
@Commit
public class QuerydslTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 1, teamA);
        Member member2 = new Member("member2", 2, teamA);
        Member member3 = new Member("member3", 3, teamB);
        Member member4 = new Member("member4", 4, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void search(){
queryFactory
        .select(member, team)
        .from(member)
        .leftJoin(member.team, team)
        .on(team.name.eq("teamA"))
        .fetch();

    }
}
