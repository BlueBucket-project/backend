package com.example.shopping.service.board;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.CreateBoardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    // 게시글 등록
    ResponseEntity<?> saveBoard(CreateBoardDTO boardDTO,
//                                List<MultipartFile> boardFiles,
                                String memberEmail) throws Exception;

    // 게시글 상세정보
    ResponseEntity<BoardDTO> getItem(Long boardId);


}
