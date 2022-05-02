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

            //-- JPQL -- (JPQL 실행시 flush 실행(DB 커밋))
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
//
//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "B");
//            em.persist(member1);
//            em.persist(member2);
//            System.out.println("=================");


            // 수정
//            Member member = em.find(Member.class, 150L);
//            member.setName("ZZZZ");

//            Member member = new Member(200L, "member200");
//            em.persist(member);
//
//            em.flush(); // 즉시 쿼리문 실행(영속성 컨텍스트의 변경내용을 데이터베이스에 동기화) 잘 안씀
//            System.out.println("==================");

            // 준영속 상태 (영속성 컨텍스트에서 관리 X)
//            Member member = em.find(Member.class, 150L);
//            member.setName("AAAA");
//
//            //em.detach(member); // 커밋이 안된다(select 쿼리만 실행, update 쿼리 실행 X) 거의 안씀, 특정 entity 만 준영속
//            //em.clear(); // 영속성 컨텍스트 완전히 초기화
//            //em.close(); // 영속성 컨텍스트 종료
//            Member member2 = em.find(Member.class, 150L);
//            System.out.println("==================");


            Member member = new Member();
            member.setId(2L);
            member.setUsername("B");
            member.setRoleType(RoleType.ADMIN);

            em.persist(member);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
