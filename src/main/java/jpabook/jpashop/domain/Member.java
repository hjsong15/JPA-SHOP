package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded  //내장타입
    private Address address;

    @OneToMany(mappedBy = "member")  //한명의 회원이 여러개의 상품을 주문하기 떄문에 일대다, 거울이다 = mappedBy
    private List<Order> orders = new ArrayList<>();



}
