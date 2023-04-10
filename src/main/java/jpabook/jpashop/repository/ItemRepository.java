package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {  //id값이 없다는건 == 새로 생성한 객체라는 뜻 즉, 신규등록 한다는 것.
            em.persist(item);
        } else {
            em.merge(item);  //강제 업데이트 느낌
        }
    }

    /**
     * Item 단 건 조회
     */
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    /**
     * Item 여러 건 조회
     * (단 건 조회는 find 메서드를 이용하면 되지만 여러 건 조회는 쿼리를 작성해야 함.)
     */
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
