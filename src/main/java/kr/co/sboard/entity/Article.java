package kr.co.sboard.entity;

import jakarta.persistence.*;
import kr.co.sboard.dto.ArticleDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private String title;
    private String content;

    @Builder.Default
    private int parent = 0;

    @Builder.Default
    private int comment = 0;

    private String cate;
    private int file;

    @Builder.Default
    private int hit = 0;
    private String writer;

    private String regip;

    @CreationTimestamp
    private LocalDateTime rdate;

    // mappedBy 할 때 외래키 속성 이름을 적어주어야함.
    @OneToMany(mappedBy = "ano")
    private List<File> fileList;

    private String nick;

}
