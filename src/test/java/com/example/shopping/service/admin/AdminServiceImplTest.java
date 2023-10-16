package com.example.shopping.service.admin;

import com.example.shopping.repository.board.BoardImgRepository;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.comment.CommentRepository;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.service.s3.S3ItemImgUploaderService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback(value = false)
class AdminServiceImplTest {

    // 상품 관련
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemImgRepository itemImgRepository;
    @Autowired
    private S3ItemImgUploaderService s3ItemImgUploaderService;
    // 게시글 관련
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardImgRepository boardImgRepository;
    // 댓글 관련
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void removeItem() {
    }

    @Test
    void removeBoard() {
    }

    @Test
    void removeComment() {
    }

    @Test
    void superitendItem() {
    }

    @Test
    void getItem() {
    }
}