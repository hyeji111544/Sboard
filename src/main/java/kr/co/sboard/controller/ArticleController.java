package kr.co.sboard.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sboard.config.AppInfo;
import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.dto.PageRequestDTO;
import kr.co.sboard.dto.PageResponseDTO;
import kr.co.sboard.entity.Article;
import kr.co.sboard.service.ArticleService;
import kr.co.sboard.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final FileService fileService;

    private final AppInfo appInfo;

    /*
        @ModelAttribute("cate")
         - modelAttribute("cate", cate)와 동일
    */
    @GetMapping("/article/list")
    public String list(Model model, String cate, PageRequestDTO pageRequestDTO) {

        PageResponseDTO pageResponseDTO = null;

        if(pageRequestDTO.getKeyword() == null) {
            // 일반 글 목록 조회
            pageResponseDTO = articleService.selectArticles(pageRequestDTO);
        }else {
            // 검색 글 목록 조회
            pageResponseDTO = articleService.searchArticles(pageRequestDTO);
        }

        log.info("pageResponseDTO : " + pageResponseDTO);

        model.addAttribute(pageResponseDTO);

        return "/article/list";
    }

    @GetMapping("/article/write")
    public String write(Model model, @ModelAttribute("cate") String cate, PageRequestDTO pageRequestDTO){

        PageResponseDTO pageResponseDTO = PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .build();

        model.addAttribute(pageResponseDTO);

        log.info("write ...:" + cate);
        return "/article/write";
    }

    @PostMapping("/article/write")
    public String write(Model model, HttpServletRequest req, ArticleDTO articleDTO ){
        /*
            글작성을 테스트할 때는 로그인해야하기 때문에
            SecurityConfig 인가 설정 수정할 것
        */
        String regip = req.getRemoteAddr();
        articleDTO.setRegip(regip);

        log.info(articleDTO.toString());
        String cate = articleDTO.getCate();


       Article article= articleService.insertArticle(articleDTO);
       int no =article.getNo();

        return "redirect:/article/list?cate="+cate;
    }

    @PostMapping("/article/modify")
    public String modify(Model model, HttpServletRequest req, ArticleDTO articleDTO){
        String regip = req.getRemoteAddr();
        articleDTO.setRegip(regip);

        log.info(articleDTO.toString());
        String cate = articleDTO.getCate();


        Article article= articleService.updateArticle(articleDTO);
        int no =article.getNo();

        return "redirect:/article/list?cate="+cate;
    }

    @GetMapping("/article/view")
    public String view(Model model, int no , int pg){
        ArticleDTO articleDTO = articleService.findById(no);
        model.addAttribute(articleDTO);
        model.addAttribute("pg", pg);
        log.info(articleDTO.toString());
        return "/article/view";
    }

    // fileDownload 메서드 FileController 로 이동
    @GetMapping("/article/modify")
    public String modify(Model model, int no , int pg){
        ArticleDTO articleDTO = articleService.findById(no);
        model.addAttribute(articleDTO);
        model.addAttribute("pg", pg);
        log.info("pg : " + pg);
        log.info(articleDTO.toString());
        return "/article/modify";
    }
/**
    @PostMapping("/article/modify")
    public String modifyArticle(@RequestBody ArticleDTO articleDTO, HttpServletRequest req){
        log.info(articleDTO.toString());
        String cate = articleDTO.getCate();

        articleService.updateArticle(articleDTO);

        return "redirect:/article/list?cate="+cate;

    }
 **/

    @DeleteMapping("/article/{no}")
    public ResponseEntity<?> deleteArticle(@PathVariable("no") int no){

        return articleService.deleteArticle(no);
    }




}