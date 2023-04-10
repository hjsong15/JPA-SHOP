package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {
    /**
     * 상품들의 공통 속성
     */
    private Long id; //상품 수정을 위한 id
    private String name;
    private int price;
    private int stockQuantity;

    /**
     * Book에 관련된 속성
     */
    private String author; //저자
    private String isbn; //넘버
}
