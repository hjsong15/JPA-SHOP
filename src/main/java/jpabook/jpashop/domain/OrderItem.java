package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //OrderService 등 다른 클래스에서 new 키워드로 생성하지 말라고 막아 놓는 protected 생성자!!!
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    //==셍성 메서드 ==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) { // orderPrice 를 따로 넣은 이유는, 쿠폰이나 할인을 적용할 수도 있기 때문.
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); //아이템 재고 차감
        return orderItem;
    }


    //==비즈니스 로직 ==//
    public void cancel() {
        getItem().addStock(count);  //재고 수량 원복
        
    }
    //==조회 로직 ==//

    /**
     * 주문 상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
