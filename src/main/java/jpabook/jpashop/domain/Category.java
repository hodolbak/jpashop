package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    // 실제적인 카테고리아이템의 테이블의 객체를 만들어서 다대다를 해결한 것이 아니기에 다대다 해소를 위한 테이블을 생성해야 하기에 JoinColumn 이 아닌 JoinTable 을 사용한다.
    // joinColumns 는 category -> category_item 으로 연결하기 위한 column
    // inverseJoinColumns category_item -> item 으로 연결하기 위한 column
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")         // 부모카테고리와 Join 하기 위해서 DB의 category.parent_id 컬럼을 사용해서 join 하겠다.
    private Category parent;

    @OneToMany(mappedBy = "parent")         // 자식카테고리는 Category.parent 와 매핑되어 있다. mappedBy Category.parent
    private List<Category> child = new ArrayList<>();

    // 연관관계 편의메서드
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
