package kr.co.sboard.service;

import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.entity.Article;
import kr.co.sboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final ArticleRepository articleRepository;
    public final ModelMapper modelMapper;

    public ResponseEntity<Article> insertComment(ArticleDTO articleDTO){
       Article article = modelMapper.map(articleDTO, Article.class);
        log.info("insertComment : " + article.toString());

        Article savedArticle = articleRepository.save(article);

        return ResponseEntity.ok().body(savedArticle);
    }

    public ResponseEntity<List<ArticleDTO>> selectComments(int no){

        // ArticleRepository > findByParent() 쿼리 메서드 정의
        List<Article> articleList = articleRepository.findByParent(no);

        List<ArticleDTO> articleDTOS = articleList.stream()
                                        .map(entity -> modelMapper.map(entity, ArticleDTO.class))
                                        .toList();

        return ResponseEntity.ok().body(articleDTOS);

    }

    public ResponseEntity<?> deleteComment(int no){
        log.info("no : "+ no);
        //삭제 전 조회
        Optional<Article> optArticle = articleRepository.findById(no);

        log.info("optArticle : " + optArticle);

        if(optArticle.isPresent()){
            log.info("deleteComment.....1");

            articleRepository.deleteById(no);

            return ResponseEntity
                    .ok()
                    .body(optArticle.get());
        }else {
            log.info("deleteComment.....2");

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("not found");
        }
    }

    public ResponseEntity<?> updateComment(ArticleDTO articleDTO){

        // 수정 전 존재여부 확인
        Optional<Article> optArticle = articleRepository.findById(articleDTO.getNo());

        if(optArticle.isPresent()){

            Article article = optArticle.get();
            //Article Entity에 @Setter 선언함..
            article.setContent(articleDTO.getContent());

            log.info("updateComment....1 : " + article);

            Article modifiedArticle = articleRepository.save(article);

            // 수정 후 수정 데이터 반환
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(modifiedArticle);
        }else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("not found");
        }
    }


}
