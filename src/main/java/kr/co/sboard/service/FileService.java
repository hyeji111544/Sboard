package kr.co.sboard.service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.dto.FileDTO;
import kr.co.sboard.entity.Article;
import kr.co.sboard.repository.ArticleRepository;
import kr.co.sboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final ArticleRepository articleRepository;

    @Value("${file.upload.path}")
    private  String fileUploadPath;

    public List<FileDTO> fileUpload(ArticleDTO articleDTO){
        String path = new File(fileUploadPath).getAbsolutePath();  //실제 업로드 파일을 구하는 것

        // 파일 정보 리턴을 위한 리스트
        List<FileDTO> files = new ArrayList<>();

        log.info("fileUploadPath..1 : " + path);

        for(MultipartFile mf : articleDTO.getFiles()){
            log.info("oName..2 : ");

            if(!mf.isEmpty()){

                String oName = mf.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf(".")); //확장자
                String sName = UUID.randomUUID().toString()+ ext;

                log.info("sName..3 : "+sName);

                try {
                    mf.transferTo(new File(path, sName));

                    //파일 정보 생성
                    FileDTO fileDTO = FileDTO.builder()
                            .oName(oName)
                            .sName(sName)
                            .build();

                    log.info("uploadFile..4 : " + fileDTO);
                    //리스트 저장
                    files.add(fileDTO);

                }catch (IOException e){
                    log.error("fileUpload : " + e.getMessage());
                }
            }
        }

        return files;
    }
    public Integer deleteFile(HttpServletRequest req, int fno){

        //파일 조회
        Optional<kr.co.sboard.entity.File> optFile = fileRepository.findById(fno);
        log.info("fileDelete...1" + optFile);

        if (optFile.isPresent()){

            kr.co.sboard.entity.File file = optFile.get();
            int ano = file.getAno(); // 파일과 연관된 게시글 번호를 가져옴

            log.info("fileDelete...2");
            fileRepository.deleteById(fno);

            // 업로드 디렉토리 파일 삭제
            ServletContext ctx = req.getServletContext();
            String uploadPath = ctx.getRealPath("/uploads");

            // 파일 객체 생성
            java.io.File fileObj = new java.io.File(uploadPath + java.io.File.separator + file.getSName());

            // 파일 삭제
            if(fileObj.exists()) {
                fileObj.delete();
            }

            Optional<Article> optArticle = articleRepository.findById(ano);
            if(optArticle.isPresent()){
                log.info("fileDelete...4" + optArticle.toString());
                return optArticle.get().getNo();
                //반환된 ano 값
            }else {
                log.info("fileDelete...5");
                return null;
            }

        }else {
            log.info("fileDelete...6");
            return null;
        }

    }

    public int deleteFiles(HttpServletRequest req, int ano){
        fileRepository.deleteById(ano);

        // 업로드 디렉토리 파일 삭제
        ServletContext ctx = req.getServletContext();
        String uploadPath = ctx.getRealPath("/uploads");


        return ano;

    }

    @Transactional
    public ResponseEntity<?> fileDownload(int fno) {

        //파일 조회
        kr.co.sboard.entity.File file = fileRepository.findById(fno).get();
        log.info("fileDownload...1");

        try {
            Path path = Paths.get(fileUploadPath + file.getSName());
            String contentType = Files.probeContentType(path);

            log.info("fileDownload...2");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(file.getOName(), StandardCharsets.UTF_8).build());

            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            Resource resource = new InputStreamResource(Files.newInputStream(path));
            log.info("fileDownload...3");

            // 파일 다운로드 카운트 업데이트
            file.setDownload(file.getDownload() + 1);
            fileRepository.save(file);
            log.info("fileDownloadCount...");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }catch (IOException e){
            log.error("fileDownload : " + e.getMessage());
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> fileDownloadCount(int fno){
        // 파일 조회
        kr.co.sboard.entity.File file = fileRepository.findById(fno).get();
        log.info("fileCount...1 : " + file);
        // 다운로드 카운트 Json 생성
        Map<String, Object> resultMap = new HashMap<>();

        log.info("fileCount...2 : " + resultMap);

        resultMap.put("count", file.getDownload());

        log.info("fileCount...3 : " + resultMap);

        return ResponseEntity.ok().body(resultMap);
    }
}
