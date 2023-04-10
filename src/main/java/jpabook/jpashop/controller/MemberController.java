package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    /**
     * 회원가입, (회원 이름이 Null이면 입력하라는 메세지 출력)
     */
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());  //form에서 name을 가져온다.
        member.setAddress(address); //위에서 만든 address를 가져온다.

        memberService.join(member);

        return "redirect:/"; // 저장된 후, 재 로딩되면 안좋기 때문에 리다이렉트를 쓰는게 조금 더 나음. / -> 첫 번째 페이지로 넘어감.

    }

    /**
     * 회원목록 조회  (Model 객체를 통해서 View로 전달)
     */
    @GetMapping("/members")
    public String list(Model model) {
//  여기서는 간단한 기능이기에 Member(List<Member>) 엔티티를 그대로 가져다 썼지만, 실무에서는 꼭!!
//  DTO로 변환해서 화면에서 꼭 필요한 데이터들만 가지고 출력해야 한다.!!
        List<Member> members = memberService.findMembers();//JPQL로 쿼리를 짜서 모든 멤버를 조회한다
        model.addAttribute("members", members); // 모델에 담고
        return "members/memberList"; //화면에 넘긴다.
    }


}
