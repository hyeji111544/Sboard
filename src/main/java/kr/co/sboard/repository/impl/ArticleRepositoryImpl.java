package kr.co.sboard.repository.impl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.sboard.dto.PageRequestDTO;
import kr.co.sboard.entity.Article;
import kr.co.sboard.entity.QArticle;
import kr.co.sboard.entity.QUser;
import kr.co.sboard.repository.custom.ArticleRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private QArticle qArticle = QArticle.article;
    private QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectArticles(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String cate = pageRequestDTO.getCate();

        log.info("selectArticles...1-1 : " + cate);

        // 부가적인 Query 실행 정보를 처리하기 위해 fetchResults()로 실행
        QueryResults<Tuple> results = jpaQueryFactory
                                            .select(qArticle, qUser.nick)
                                            .from(qArticle)
                                            .where(qArticle.cate.eq(cate).and(qArticle.parent.eq(0)))
                                            .join(qUser)
                                            .on(qArticle.writer.eq(qUser.uid))
                                            .offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .orderBy(qArticle.no.desc())
                                            .fetchResults();

        log.info("selectArticles...1-2 : " + results);


        List<Tuple> content = results.getResults();

        log.info("selectArticles...1-3 : " + content);

        long total = results.getTotal();


        // 페이징 처리를 위해 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Tuple> searchArticles(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String cate = pageRequestDTO.getCate();
        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        // 검색 종류에 따른 where 표현식 생성
        BooleanExpression expression = null;

        if(type.equals("title")) {
            expression = qArticle.cate.eq(cate).and(qArticle.title.contains(keyword));
            log.info("expression : " + expression);

        }else if(type.equals("content")) {
            expression = qArticle.cate.eq(cate).and(qArticle.parent.eq(0)).and(qArticle.content.contains(keyword));
            log.info("expression : " + expression);

        }else if(type.equals("title_content")) {
            BooleanExpression titleContains = qArticle.title.contains(keyword);
            BooleanExpression contentContains = qArticle.content.contains(keyword);
            expression = qArticle.cate.eq(cate).and(qArticle.parent.eq(0)).and(titleContains.or(contentContains));
            log.info("expression : " + expression);

        }else if(type.equals("writer")) {
            expression = qArticle.cate.eq(cate).and(qArticle.parent.eq(0)).and(qArticle.nick.contains(keyword));
            log.info("expression : " + expression);
        }

        // 부가적인 Query 실행 정보를 처리하기 위해 fetchResults()로 실행
        QueryResults<Tuple> results = jpaQueryFactory
                                        .select(qArticle, qUser.nick)
                                        .from(qArticle)
                                        .join(qUser)
                                        .on(qArticle.writer.eq(qUser.uid))
                                        .where(expression)
                                        .offset(pageable.getOffset())
                                        .limit(pageable.getPageSize())
                                        .orderBy(qArticle.no.desc())
                                        .fetchResults();


        List<Tuple> content = results.getResults();
        long total = results.getTotal();


        // 페이징 처리를 위해 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }

}