package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name ="order_id")
    private Long id;

    // 주문 N:1 주문회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 회원테이블과 Join 하기위해서 DB의 oders.member_id 컬럼을 사용해서 join 하겠다.
    private Member member;

    // 주문 1:N 주문상품
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)       // 주문의 상품리스트는 주문상품객체의 주문과 매핑되어 있어. mappedBy : OrderItem.order
    private List<OrderItem> orderItems = new ArrayList<>();

    // 주문 1:1 배송정보
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)    // 배송테이블과 Join 하기위해서 DB의 oders.delivery_id 컬럼을 사용해서 join 하겠다.
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;    // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;         // 주문상태 [ORDER, CANCEL]

    // 연관 관계 편의 메서드
    // 양방향 관계인 경우 각 속성에 값을 대입해야 한다.
    // FK 가 아닌 주요 업무 객체에서 코딩을 한다.
    // 주문 N:1 회원
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    // 주문 1:N 상품
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // 주문 1:1 배송정보
    public void setDelevery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();

        order.setMember(member);
        order.setDelevery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완됴된 상품ㅇ느 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    // 조회 로직
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
