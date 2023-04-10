package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item", //중간테이플 매핑
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    //카테고리 구조 볼 수 있게 (부모 관계까지)
    @ManyToOne(fetch = LAZY) // 내 부모니까
    @JoinColumn(name = "parent_id")
    private Category parent;

    //내 자식을 보는 법
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    //==연관관계 편의 메서드 ==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }


}
