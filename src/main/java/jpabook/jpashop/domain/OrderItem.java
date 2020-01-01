package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    // 주문상품 N:1 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")       // 상품테이블과 조인하기 위해서 DB의 order_item.item_id 컬럼을 사용해서 join 하겠다.
    private Item item;

    // 주문상품 N:1 주문
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")      // 주문테이블과 조인하기 우해서 DB의 order_item.oder_id 컬럼을 사용해서 join 하겠다.
    private Order order;

    private int orderPrice;
    private int count;

    // 생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    public void cancel(){
        getItem().addStock(count);
    }

    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }
}
