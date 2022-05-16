package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.concurrent.locks.Lock;


public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        //transaction 얻기
        EntityTransaction tx = em.getTransaction();
        tx.begin();


        try {

            Locker locker = new Locker();
            locker.setName("locker01");
            em.persist(locker);

            em.flush();


            Member member = new Member();
            member.setUserName("test01");
            member.setLocker(locker);
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
