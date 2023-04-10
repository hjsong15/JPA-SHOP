package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    /**
     * 상품 주문 (목록으로 member와 item을 보고 선택하는 방식)
     */
    @GetMapping("/order")
    public String createForm(Model model) {

        List<Member> members = memberService.findMembers(); // 모든 멤버를 가져온다.
        List<Item> items = itemService.findItems(); //모든 아이템을 다 가져온다

        model.addAttribute("members", members);
        model.addAttribute("items", items);  // member와 item모두 model에 담아서

        return "order/orderForm"; // orderForm으로 넘긴다.
    }

    /**
     * 상품 주문에서 Submit 클릭 시, post요청
     * orderForm 에서 설정해놓은 파라미터들
     */
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count);
        return "redirect:/orders";  //orders -> 주문내역 목록

    }

    /**
     * 주문 내역 조회
     */
    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";

    }

    /**
     * 주문 삭제
     */
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
