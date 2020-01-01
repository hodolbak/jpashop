package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // 멤버 1:N 주문
    @OneToMany(mappedBy = "member")     // 회원의 주문리스트는 주문객체의 주문회원과 매핑이 되어 있다. mappedBy : Order.member
    private List<Order> orders = new ArrayList<>();

}