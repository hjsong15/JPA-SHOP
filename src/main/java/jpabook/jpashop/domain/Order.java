package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)// XToOne 은 디폴트 값이 즉시로딩이기 때문에 지연로딩으로 꼭 설정해줘야 N+1문제가 생기지 않는다.
    @JoinColumn(name = "member_id") //매핑 지정(FK)
    private Member member;  // 주문 회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     *  Order 랑 Delivery 는 1:1 매핑이기 때문에 어디에 FK를 둬도 상관 없지만 Order를 통해 배송을 조회하기 떄문에 Order에 FK를 잡는다.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //배송정보

    private LocalDateTime orderDate;  //주문시간

    @Enumerated(EnumType.STRING)  // ORDINAL은 숫자로만 나오기 떄문에 나중에 장애가 난다. 무조건 STRING으로 할 것.
    private OrderStatus status; // 주문상태[ORDER, CANCEL] enum클래스


    // 연관관계 편의 메서드 (양방향 세팅)//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //중요! == 생성 메서드 ==//  주문 생성관련 수정은 여기서만 하면 됨, 이러한 장점으로 인하여 생성메서드 생성.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직 ==//

    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) { // 배송이 끝나서 취소 불가능, COMP = 주문 완료
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능 합니다.");
        }
        //위 메서드 통과시 취소, 및 재고 원복
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) { //한번에 여러 개를 주문 할 수 있으니 각각 취소
            orderItem.cancel();
        }
    }

    //==조회 로직 ==//

    /**
     *  전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
