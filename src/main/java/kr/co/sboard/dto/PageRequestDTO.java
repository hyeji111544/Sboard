package kr.co.sboard.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.awt.print.Pageable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PageRequestDTO {

    @Builder.Default
    private int no = 1;

    @Builder.Default
    private int pg = 1;

    @Builder.Default
    private int size = 10;

    private String cate;

    /* 검색을 위한 type, keyword 선언 */
    private String type;
    private String keyword;

    public PageRequest getPageable(String sort){

        return PageRequest.of(this.pg -1, this.size, Sort.by(sort).descending());
    }
}
