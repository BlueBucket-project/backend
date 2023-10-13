package com.example.shopping.service.admin;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.board.BoardImgEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.repository.board.BoardImgRepository;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.comment.CommentRepository;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.service.s3.S3ItemImgUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    // 상품 관련
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final S3ItemImgUploaderService s3ItemImgUploaderService;
    // 게시글 관련
    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    // 댓글 관련
    private final CommentRepository commentRepository;

    // 상품 삭제
    @Override
    public String removeItem(Long itemId, UserDetails userDetails) {
        try {
            // 삭제할 권한이 있는지 확인
            // userDetails에서 권한을 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            // GrantedAuthority 타입의 권한을 List<String>으로 담아줌
            List<String> collectAuthorities = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 상품 조회
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            // 상품 이미지 조회
            List<ItemImgEntity> findItemImg = itemImgRepository.findByItemItemId(itemId);

            // 권한이 있는지 체크
            if (!collectAuthorities.isEmpty()) {
                for (String role : collectAuthorities) {
                    // 존재하는 권한이 관리자인지 체크
                    if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                        // 삭제하는데 이미지를 풀어놓는 이유는
                        // S3에 삭제할 때 넘겨줘야 할 매개변수때문이다.
                        for (ItemImgEntity itemImgEntity : findItemImg) {
                            String uploadImgPath = itemImgEntity.getUploadImgPath();
                            String uploadImgName = itemImgEntity.getUploadImgName();

                            // 상품 정보 삭제
                            itemRepository.deleteByItemId(findItem.getItemId());
                            // S3에서 이미지 삭제
                            s3ItemImgUploaderService.deleteFile(uploadImgPath, uploadImgName);
                        }
                    } else {
                        return "삭제할 권한이 없습니다.";
                    }
                }
                return "상품을 삭제 했습니다.";
            } else {
                return "권한 정보가 비어 있습니다.";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // 게시물 삭제
    @Override
    public String removeBoard(Long boardId, UserDetails userDetails) {
        try {
            // 삭제할 권한이 있는지 확인
            // userDetails에서 권한을 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            // GrantedAuthority 타입의 권한을 List<String>으로 담아줌
            List<String> collectAuthorities = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 상품 조회
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            // 상품 이미지 조회
            List<BoardImgEntity> findBoardImg = boardImgRepository.findByBoardBoardId(boardId);

            // 권한이 있는지 체크
            if (!collectAuthorities.isEmpty()) {
                for (String role : collectAuthorities) {
                    // 존재하는 권한이 관리자인지 체크
                    if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                        // 삭제하는데 이미지를 풀어놓는 이유는
                        // S3에 삭제할 때 넘겨줘야 할 매개변수때문이다.
                        for (BoardImgEntity boardImgEntity : findBoardImg) {
                            String uploadImgPath = boardImgEntity.getUploadImgPath();
                            String uploadImgName = boardImgEntity.getUploadImgName();

                            // 상품 정보 삭제
                            itemRepository.deleteByItemId(findBoard.getBoardId());
                            // S3에서 이미지 삭제
                        }
                    } else {
                        return "삭제할 권한이 없습니다.";
                    }
                }
                return "상품을 삭제 했습니다.";
            } else {
                return "권한 정보가 비어 있습니다.";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // 예약 상품 전체 페이지 조회
    @Override
    public Page<ItemDTO> superitendItem(Pageable pageable, UserDetails userDetails) {
        try {
            // userDetails에서 권한을 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            // GrantedAuthority 타입의 권한을 List<String>으로 담아줌
            List<String> collectAuthorities = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());


            for (String role : collectAuthorities) {
                // 존재하는 권한이 관리자인지 체크
                if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                    // 페이지 처리해서 예약된 것만 조회
                    Page<ItemEntity> items =
                            itemRepository.findByItemSellStatus(pageable, ItemSellStatus.RESERVED);
                    log.info("items : {}", items);
                    return items.map(ItemDTO::toItemDTO);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
