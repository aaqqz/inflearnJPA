package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;


public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        //transaction 얻기
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team1 = new Team();
            team1.setName("팀A");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(team2);
            em.persist(member3);

//            em.flush();
//            em.clear();


            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            em.clear();

            Member member = em.find(Member.class, member1.getId());
            System.out.println("member = " + member);


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
