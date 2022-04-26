package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;


public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        //transaction 얻기
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        //Member member = new Member();

        try {
            // 1, 회원 등록 S
//            member.setId(1L);
//            member.setName("helloJPA");
//            em.persist(member);
            // 1, 회원 등록 E

            // 2, 회원 수정 S
//            Member findMember = em.find(Member.class, 1L); // 단건 조회
//            findMember.setName("helloJPA");
            // 2, 회원 수정 E

            // 3, 회원 삭제 S
//            em.remove(findMember);
            // 3, 회원 삭제 S

            //-- JPQL --
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            for (Member member : result) {
                System.out.println("member.name = " + member.getName());
            }
            //-- JPQL --

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
