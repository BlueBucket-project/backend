package com.example.shopping.service.comment;

import com.example.shopping.domain.board.BoardSecret;
import com.example.shopping.domain.board.ReplyStatus;
import com.example.shopping.domain.comment.CommentDTO;
import com.example.shopping.domain.comment.UpdateCommentDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.Container.ContainerEntity;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.comment.CommentEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.comment.CommentRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Log4j2
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private  MemberRepository memberRepository;
    @Mock
    private BoardRepository boardRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

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

    private CommentEntity createComment() {
        return CommentEntity.builder()
                .commentId(1L)
                .comment("내용")
                .member(createMember())
                .board(createBoard())
                .build();
    }

    @Test
    @DisplayName("댓글 생성 서비스 테스트")
    void save() {
        // given
        CommentEntity comment = createComment();
        UpdateCommentDTO updateCommentDTO = UpdateCommentDTO.builder()
                .comment("취업해야해")
                .build();

        MemberEntity member = createMember();
        BoardEntity board = createBoard();
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(commentRepository.save(any())).willReturn(comment);

        // when
        commentService.save(1L, updateCommentDTO, member.getEmail());

        // then
        verify(commentRepository).save(any());
    }

    @Test
    @DisplayName("댓글 삭제 서비스 테스트")
    void remove() {
        // given
        CommentEntity comment = createComment();
        MemberEntity member = createMember();
        UserDetails userDetails = User
                .withUsername(member.getEmail())
                .password(member.getMemberPw())
                .authorities(new SimpleGrantedAuthority("ROLE_ADIN"))
                .build();

        BoardEntity board = createBoard();
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        doNothing().when(commentRepository).deleteById(anyLong());

        // when
        commentService.remove(comment.getBoard().getBoardId(), comment.getCommentId(), userDetails);

        // then
        verify(commentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("댓글 수정 서비스 테스트")
    void update() {
        // given
        CommentEntity comment = createComment();
        UpdateCommentDTO updateCommentDTO = UpdateCommentDTO.builder()
                .comment("수정된 내용")
                .build();

        CommentDTO inputComment = CommentDTO.builder()
                .commentId(comment.getCommentId())
                .comment(updateCommentDTO.getComment())
                .writeTime(comment.getRegTime())
                .build();

        MemberEntity member = createMember();
        BoardEntity board = createBoard();
        CommentEntity commentEntity = CommentEntity.toCommentEntity(inputComment, member, board);

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        given(commentRepository.save(any())).willReturn(commentEntity);

        // when
        commentService.update(
                comment.getBoard().getBoardId(),
                comment.getCommentId(),
                updateCommentDTO,
                member.getEmail());

        // then
        verify(commentRepository).save(any());
    }
}