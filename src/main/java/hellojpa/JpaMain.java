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
//            List<Member> result = em.createQuery("select m from Member m", Member.class)
//                    .setFirstResult(1)
//                    .setMaxResults(10)
//                    .getResultList();
//
//            for (Member member : result) {
//                System.out.println("member.name = " + member.getName());
//            }
            //-- JPQL --

            // 엔티티를 생성한 상태(비영속) - JPA 랑 관련이 없다
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("helloJpa");
//
//            // 엔티티를 (영속) JPA가 관리
//            em.persist(member);
            //em.detach(member); // 영속 상태에서 제거 (영속상태에서 제거되어 insert 실행 안됨)


//            Member findMember1 = em.find(Member.class, 101L);
//            Member findMember2 = em.find(Member.class, 101L);
//            System.out.println("result = " + (findMember1 == findMember2 ));

            Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");
            em.persist(member1);
            em.persist(member2);
            System.out.println("=================");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
