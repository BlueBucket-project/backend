package com.example.shopping.service.board;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.CreateBoardDTO;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.comment.CommentEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.board.BoardImgRepository;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.service.s3.S3BoardImgUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class BoardServiceImpl implements BoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    private final S3BoardImgUploaderService s3BoardImgUploaderService;

    // 게시글 등록 메소드
    @Override
    public ResponseEntity<?> saveBoard(CreateBoardDTO boardDTO,
                                       String memberEmail) throws Exception {
        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);
        log.info("user : " + findUser);

        if (findUser != null) {
            BoardEntity boardEntity = BoardEntity.createBoard(boardDTO, findUser);

            BoardEntity saveBoard = boardRepository.save(boardEntity);
            BoardDTO returnBoard = BoardDTO.toBoardDTO(saveBoard);
            return ResponseEntity.ok().body(returnBoard);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
        }
    }


}