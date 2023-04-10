package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    /**
     * 저장
     */
    public void save(Member member) {
        em.persist(member);
    }

    /**
     * 단건조회
     */
    public Member findOne(Long id) {
        return em.find(Member.class, id); //JPA가 제공하는 find 메서드를 이용하여 조회한다. (타입, PK)
    }

    /**
     * 리스트 조회  JPQL쿼리를 작성해줘야 함.  기본 SQL은 테이블을 대상으로, JPQL은 객체를 대상으로.
     */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class) //작성방법 -> JPQL, 반환타임
                .getResultList();   // 멤버를 리스트로 만들어줌.
    }

    /**
     *이름으로 조회 (파라미터 바인딩)
     */
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }


}
