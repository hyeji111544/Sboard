package kr.co.sboard.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.sboard.dto.PageRequestDTO;
import kr.co.sboard.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {


    public Page<Tuple> selectArticles(PageRequestDTO pageRequestDTO, Pageable pageable);

    public Page<Tuple> searchArticles(PageRequestDTO pageRequestDTO, Pageable pageable);

}
