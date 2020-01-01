package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)           // 상속을 처리하기 위한 테이블 생성 전략.
@DiscriminatorColumn(name = "dtype")                            // 싱글테이블이기 때문에 각 자식객체의 구분자를 dtype 컬럼의 값으로 구분한다.
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // 상품 N:N 상품군
    @ManyToMany(mappedBy = "items")                             // 카테고리객체의 Category.items 에 매핑되어 있다.
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직 //
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
