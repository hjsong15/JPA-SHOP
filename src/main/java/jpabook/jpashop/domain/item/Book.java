package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")//싱글톤이기 때문에 구분할 수 있어야 한다.
@Getter
@Setter
public class Book extends Item{

    private String author;
    private String isbn;

}
