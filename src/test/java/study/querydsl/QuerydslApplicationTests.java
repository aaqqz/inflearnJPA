package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import study.querydsl.entity.Hello;
import study.querydsl.entity.QHello;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class QuerydslApplicationTests {

	// todo 테스트 검증 코드 assertj 로 변경

	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {
		// querydsl test
		Hello hello = new Hello();
		em.persist(hello);

		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		QHello qHello = QHello.hello;

		Hello result = queryFactory.selectFrom(qHello)
				.fetchOne();

		assertEquals(hello, result);
		assertEquals(hello.getId(), result.getId());
	}

}
