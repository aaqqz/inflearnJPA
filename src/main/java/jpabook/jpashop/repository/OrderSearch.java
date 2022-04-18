package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

@Getter @Setter
public class OrderSearch {

    private String memberName; //회원이름
    private OrderStatus orderStatus; //주문 상태[ORDER, CANCEL]
}
