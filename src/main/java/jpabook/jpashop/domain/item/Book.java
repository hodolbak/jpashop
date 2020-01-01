package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")        // DiscriminatorColumn 인 dtype 값을 B로 하겠다.
@Getter
@Setter
public class Book extends Item {
    private String author;
    private String isbn;
}
