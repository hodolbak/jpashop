package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")    // DiscriminatorColumn 인 dtype 값을 A로 하겠다.
@Getter
@Setter
public class Album extends Item{
    private String artist;
    private String etc;
}
