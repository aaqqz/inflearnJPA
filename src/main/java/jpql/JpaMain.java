package jpql;

import javax.persistence.*;


public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        //transaction 얻기
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("test");
            member.setAge(10);
            em.persist(member);

            // 반환 타입이 명확할 때 사용
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);

            // 반환 타입이 명확하지 않을 때 사용
            Query query = em.createQuery("select m.username, m.age from Member m", Member.class);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
