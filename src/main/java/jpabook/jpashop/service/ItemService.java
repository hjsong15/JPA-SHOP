package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * Item 저장
     */
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 상품 수정
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item item = itemRepository.findOne(itemId);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
    }

    /**
     * Item 여러개 조회
     */
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    /**
     * Item 한 개 조회
     */
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
