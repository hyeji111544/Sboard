package kr.co.sboard.dto;

import jakarta.mail.Multipart;
import kr.co.sboard.entity.Article;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ArticleDTO {
    private int no;
    private int parent;
    private int comment;
    private String cate;
    private String title;
    private String content;
    private int file;
    private int hit;
    private String writer;
    private String regip;
    private LocalDateTime rdate;

    private List<MultipartFile> files;

    private UserDTO user;
    private List<FileDTO> fileList;

    private String nick;

}
