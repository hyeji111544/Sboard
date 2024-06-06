package kr.co.sboard.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sboard.service.ArticleService;
import kr.co.sboard.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;
    private final ArticleService articleService;

    @GetMapping("/file/fileDownload/{fno}")
    public ResponseEntity<?> fileDownload(@PathVariable("fno") int fno){
        log.info("fileDownload : " + fno);
        return fileService.fileDownload(fno);
    }

    @GetMapping("/file/downloadCount/{fno}")
    public ResponseEntity<?> fileDownloadCount(@PathVariable("fno") int fno){
        log.info("fileDownloadCount : " + fno);
        return fileService.fileDownloadCount(fno);
    }


    @PostMapping("/file/modifyFile")
    public void deleteFile(@RequestBody Map<String , List<Integer>> map, HttpServletRequest req){
        List<Integer> fnolists =  map.get("fno");
        log.info("deleteFile!!!!"+fnolists.get(0).toString());
        // 파일 갯수
        for(Integer fno : fnolists){
            Integer ano = fileService.deleteFile(req, fno);
            log.info("deleteFile!!2...:" + ano);

            articleService.updateArticleForFileCount(ano);

        }
    }


}
