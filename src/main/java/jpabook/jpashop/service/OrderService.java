package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId); //멤버 아이디를 찾고
        Item item = itemRepository.findOne(itemId); // 아이템 아이디를 가져온다.

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());// 회원 주소로 배송한다. *실제 쇼핑몰은 주소를 다시 입력해야함.

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);// count = 주문 수량

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order); //Order 클래스에서 cascade=ALL 을 해놨기 때문에 orderItem, Delivery가 자동으로 Persistence 된 상태.
        /** cascade는 하나를 수정하면 연관된 객체(엔티티)들도 다 변경되기 때문에 주의해서 사용해야 함.
        이 예제는, Order만 Delivery를 사용하고, Order만 OrderItem을 사용하기 때문에, 이 두 가지 조건이 충족되어 사용한 것임.
        */

        return order.getId();
    }

    /**
     * 주문 취소
     * 참고 : 주문과 특히 주문 취소 메서드는 비즈니스 로직 대부분이 엔티티에 있다.  이러한 방식을 "도메인 모델 패턴" 이라 부르고,
     * 반대로 엔티티에는 비즈니스 로직이 거의 없고, 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을 "트랜잭션 스크립트 패턴"이라 한다.
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

}
