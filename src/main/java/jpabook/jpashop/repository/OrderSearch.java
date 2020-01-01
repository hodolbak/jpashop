package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}
