package com.example.shopping.service.board;

import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.BoardSecret;
import com.example.shopping.domain.board.CreateBoardDTO;
import com.example.shopping.domain.board.ReplyStatus;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.Container.ContainerEntity;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Log4j2
class BoardServiceImplTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BoardServiceImpl boardService;


    private MemberEntity createMember() {
        MemberEntity member = MemberEntity.builder()
                .memberId(1L)
                .memberPw("dudtjq8990!")
                .memberName("테스터")
                .memberRole(Role.USER)
                .nickName("테스터")
                .email("test@test.com")
                .memberPoint(0)
                .provider(null)
                .providerId(null)
                .address(AddressEntity.builder()
                        .memberAddr("서울시 강남구")
                        .memberZipCode("103-332")
                        .memberAddrDetail("102")
                        .build())
                .build();

        given(memberRepository.findByEmail(anyString())).willReturn(member);
        return member;
    }


    ContainerEntity container = ContainerEntity.builder()
            .containerName("1지점")
            .containerAddr("서울시 고척동 130-44")
            .build();

    private ItemEntity createItem() {
        return ItemEntity.builder()
                .itemId(1L)
                .itemName("맥북")
                .itemDetail("M3입니다.")
                .itemSeller(1L)
                .itemRamount(0)
                .itemReserver(null)
                .itemImgList(new ArrayList<>())
                .boardEntityList(new ArrayList<>())
                .itemPlace(container)
                .price(1000000)
                .stockNumber(1)
                .build();
    }


    private BoardEntity createBoard() {
        return BoardEntity.builder()
                .boardSecret(BoardSecret.UN_LOCK)
                .boardId(1L)
                .title("제목")
                .content("내용")
                .member(createMember())
                .commentEntityList(new ArrayList<>())
                .replyStatus(ReplyStatus.REPLY_X)
                .item(createItem())
                .build();
    }
    private BoardEntity createBoard2() {
        return BoardEntity.builder()
                .boardSecret(BoardSecret.UN_LOCK)
                .boardId(2L)
                .title("제목2")
                .content("내용2")
                .member(createMember())
                .commentEntityList(new ArrayList<>())
                .replyStatus(ReplyStatus.REPLY_X)
                .item(createItem())
                .build();
    }

    @Test
    @DisplayName("게시글 작성 서비스 테스트")
    void saveBoard() throws Exception {
        // given
        BoardEntity board = createBoard();
        log.info("board : " + board);
        CreateBoardDTO boardDTO = CreateBoardDTO.builder()
                .title("제목")
                .content("내용")
                .build();
        ItemEntity item = createItem();
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));
        given(boardRepository.save(any())).willReturn(board);

        // when
        boardService.saveBoard(1L, boardDTO, "test@test.com");

        // then
        verify(boardRepository).save(any());
    }

    @Test
    @DisplayName("게시글 삭제 서비스 테스트")
    void removeBoard() {
        // given
        BoardEntity board = createBoard();
        UserDetails userDetails =
                User.withUsername(createMember().getEmail())
                        .password(createMember().getMemberPw())
                        // UserDetails 객체에 사용자의 권한 정보를 설정하기 위해 GrantedAuthority 객체를 사용합니다.
                        .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                        .build();
        given(boardRepository.findByBoardId(anyLong())).willReturn(Optional.of(board));
        doNothing().when(boardRepository).deleteById(anyLong());

        // when
        boardService.removeBoard(1L, userDetails);

        // then
        verify(boardRepository).deleteById(1L);
    }

    @Test
    @DisplayName("게시글 단건 조회 서비스 테스트")
    void getBoard() {
        // given
        BoardEntity board = createBoard();
        given(boardRepository.findByBoardId(anyLong())).willReturn(Optional.of(board));

        // when
        ResponseEntity<?> result = boardService.getBoard(1L, createMember().getEmail());
        BoardDTO boardDTO = (BoardDTO) result.getBody();

        // then
        Assertions.assertThat(boardDTO.getTitle()).isEqualTo(board.getTitle());
    }


    @Test
    @DisplayName("게시글 수정 서비스 테스트")
    void updateBoard() {
        // given
        BoardEntity board = createBoard();
        CreateBoardDTO boardDTO = CreateBoardDTO.builder()
                .title("제목(수정)")
                .content("내용(수정)")
                .build();

        BoardDTO inputBoard = BoardDTO.builder()
                .boardId(board.getBoardId())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .nickName(board.getMember().getNickName())
                .regTime(board.getRegTime())
                .commentDTOList(new ArrayList<>())
                .boardSecret(board.getBoardSecret())
                .replyStatus(board.getReplyStatus())
                .itemId(board.getItem().getItemId())
                .build();

        MemberEntity member = createMember();
        ItemEntity item = createItem();
        BoardEntity boardEntity = BoardEntity.toBoardEntity(inputBoard, member, item);
        given(boardRepository.findByBoardId(anyLong())).willReturn(Optional.of(board));
        given(boardRepository.save(any())).willReturn(boardEntity);

        // when
        ResponseEntity<?> responseEntity = boardService.updateBoard(1L, boardDTO, "test@test.com");
        log.info(responseEntity);
        // then
        verify(boardRepository, atLeastOnce()).save(any());
    }

    @Test
    @DisplayName("본인 게시글 전체 조회 서비스 테스트")
    void getMyBoards() {
        // given
        List<BoardEntity> boards = new ArrayList<>();
        BoardEntity board = createBoard();
        BoardEntity board2 = createBoard2();
        boards.add(board);
        boards.add(board2);

        // 페이지 번호 1, 페이지 크기 10, 정렬 기준은 "createdAt"을 사용한 Pageable 생성
        Pageable pageable =
                PageRequest.of(1, 10);

        given(boardRepository.findByMemberEmailAndTitleContaining(
                eq("test@test.com"), eq(pageable), any()))
                .will(invocation -> {
                    Pageable actualPageable = invocation.getArgument(1);
                    log.info("Actual pageable: " + actualPageable);
                    return new PageImpl<>(boards, actualPageable, boards.size());
                });


        // when
        Page<BoardDTO> result = boardService.getMyBoards(createMember().getEmail(), pageable, any());

        // then
        assertEquals(boards.size(), result.getContent().size());
        // 정렬 확인: 내림차순 정렬되었는지 확인
        assertEquals(board.getBoardId(), result.getContent().get(0).getBoardId());
        assertEquals(board2.getBoardId(), result.getContent().get(1).getBoardId());
    }

    @Test
    @DisplayName("게시글 전체 조회 서비스 테스트")
    void getBoards() {
        // given
        List<BoardEntity> boards = new ArrayList<>();
        BoardEntity board = createBoard();
        BoardEntity board2 = createBoard2();
        boards.add(board);
        boards.add(board2);

        MemberEntity member = createMember();

        // 페이지 번호 1, 페이지 크기 10
        Pageable pageable = PageRequest.of(1, 10);

        given(boardRepository.findAllByItemItemId(anyLong(), eq(pageable)))
                .will(invocation -> {
                    Pageable actualPageable = invocation.getArgument(1);
                    log.info("Actual pageable: " + actualPageable);
                    return new PageImpl<>(boards, actualPageable, boards.size());
                });

        ItemEntity item = createItem();
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));

        // when
        Page<BoardDTO> result = boardService.getBoards(pageable, 1L, member.getEmail());

        // then
        // 페이징 처리에 따른 결과 검증
        assertEquals(boards.size(), result.getContent().size());
        // 정렬 확인: 내림차순 정렬되었는지 확인
        assertEquals(board.getBoardId(), result.getContent().get(0).getBoardId());
        assertEquals(board2.getBoardId(), result.getContent().get(1).getBoardId());
    }
}