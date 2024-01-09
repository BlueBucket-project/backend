package com.example.shopping.service.admin;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.BoardSecret;
import com.example.shopping.domain.board.ReplyStatus;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartStatus;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.domain.order.OrderMainDTO;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.entity.order.OrderItemEntity;
import com.example.shopping.exception.item.ItemException;
import com.example.shopping.exception.service.OutOfStockException;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.cart.CartItemRepository;
import com.example.shopping.repository.cart.CartRepository;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.repository.order.OrderItemRepository;
import com.example.shopping.repository.order.OrderRepository;
import com.example.shopping.service.s3.S3ItemImgUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/*
 *   writer : 유요한, 오현진
 *   work :
 *          관리자 서비스
 *          - 게시글 삭제, 상품 삭제, 구매 확정 기능과 모든 문의글을 보고 특정 상품에 대한 문의글을 보는 기능이 있습니다.
 *          이렇게 인터페이스를 만들고 상속해주는 방식을 선택한 이유는
 *          메소드에 의존하지 않고 필요한 기능만 사용할 수 있게 하고 가독성과 유지보수성을 높이기 위해서 입니다.
 *   date : 2023/12/05
 * */
@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    // 상품 관련
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;

    // 유저 관련
    private final MemberRepository memberRepository;

    // 주문 관련
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // 장바구니 관련
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final S3ItemImgUploaderService s3ItemImgUploaderService;
    // 게시글 관련
    private final BoardRepository boardRepository;

    // 상품 삭제
    @Override
    public String removeItem(Long itemId, UserDetails userDetails) {
        try {
            // 삭제할 권한이 있는지 확인
            // userDetails에서 권한을 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            // 상품 조회
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            // 상품 이미지 조회
            List<ItemImgEntity> findItemImg = itemImgRepository.findByItemItemId(itemId);

            // 현재는 권한이 1개만 있는 것으로 가정
            if (!authorities.isEmpty()) {
                // 현재 사용자의 권한(authority) 목록에서 첫 번째 권한을 가져오는 코드입니다.
                // 현재 저의 로직에서는 유저는 하나의 권한을 가지므로 이렇게 처리할 수 있다.
                String role = authorities.iterator().next().getAuthority();
                log.info("권한 : " + role);
                // 존재하는 권한이 관리자인지 체크
                if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                    // 장바구니 상품을 null로 바꾸고 저장
                    List<CartItemDTO> items = cartItemRepository.findByItemId(itemId);
                    for (CartItemDTO item : items) {
                        item.setItem(null);
                        cartItemRepository.save(item);
                    }
                    // 상품 정보 삭제
                    itemRepository.deleteByItemId(findItem.getItemId());
                    // 삭제하는데 이미지를 풀어놓는 이유는
                    // S3에 삭제할 때 넘겨줘야 할 매개변수때문이다.
                    for (ItemImgEntity itemImgEntity : findItemImg) {
                        String uploadImgPath = itemImgEntity.getUploadImgPath();
                        String uploadImgName = itemImgEntity.getUploadImgName();
                        // S3에서 이미지 삭제
                        s3ItemImgUploaderService.deleteFile(uploadImgPath, uploadImgName);
                    }
                    return "상품을 삭제 했습니다.";
                }
            }
            return "상품 삭제 권한이 없습니다.";
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

            // 게시글 조회
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);

            // 권한이 있는지 체크
            if (!authorities.isEmpty()) {
                String role = authorities.iterator().next().getAuthority();
                log.info("권한 : " + role);

                if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                    // 게시글 정보 삭제
                    boardRepository.deleteByBoardId(findBoard.getBoardId());
                    return "게시글을 삭제 했습니다.";
                }
            }
            return "게시글 삭제 권한이 없습니다.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    // 구매 확정 메소드
    public OrderDTO orderItem(List<OrderMainDTO> orders, String adminEmail) {

        String mbrEmail = orders.get(0).getItemReserver();
        MemberEntity orderMember = memberRepository.findByEmail(mbrEmail);
        Long memberId = orderMember.getMemberId();
        Long adminId = memberRepository.findByEmail(adminEmail).getMemberId();

        //구매하려는 상품템리스트
        List<OrderItemDTO> itemList = new ArrayList<>();
        //주문셋팅 DTO
        OrderDTO orderInfo = null;
        //최종 주문처리상품 DTO
        OrderDTO savedOrder;

        for (OrderMainDTO order : orders) {
            ItemEntity item = itemRepository.findByItemId(order.getItemId());

            if (item.getItemSellStatus() != ItemSellStatus.RESERVED) {
                //throw 예약된 물품이 아니라 판매 못함
                throw new ItemException("예약된 물품이 아니라 구매처리 할 수 없습니다.");
            }

            if (!item.getItemReserver().equals(orderMember.getEmail())) {
                //throw 구매자와 예약한사람이 달라 판매 못함
                throw new ItemException("예약자와 현재 구매하려는 사람이 달라 구매처리 할 수 없습니다.");
            }

            if (item.getStockNumber() < order.getCount() || item.getStockNumber() == 0) {
                throw new OutOfStockException(item.getItemName() + "의 재고가 부족합니다. 요청수량 : " + order.getCount() +
                        " 재고 : " + item.getStockNumber());
            }

            // 구매처리 하려는 아이템 셋팅
            OrderItemEntity orderItem = OrderItemEntity.setOrderItem(item, memberId, item.getItemSeller(), order.getCount());
            itemList.add(orderItem.toOrderItemDTO());

            orderInfo = OrderDTO.createOrder(adminId, memberId, itemList);
        }
        // 주문처리
        savedOrder = orderRepository.save(orderInfo);

        for (OrderItemDTO savedItem : savedOrder.getOrderItem()) {

            OrderItemDTO savedOrderItem = orderItemRepository.save(savedItem, savedOrder);

            // Member-point 추가
            MemberEntity member = memberRepository.findByEmail(orderMember.getEmail());
            member.addPoint(savedOrderItem.getItemPrice() * savedOrderItem.getItemAmount());
            memberRepository.save(member);

            // Item-status 변경
            ItemEntity updateItem = itemRepository.findById(savedItem.getItemId()).orElseThrow();
            updateItem.itemSell(savedItem.getItemAmount());
            itemRepository.save(updateItem);

            // Cart-status 변경
            Long cartId = cartRepository.findByMemberMemberId(orderMember.getMemberId()).getCartId();
            CartItemDTO cartItem = cartItemRepository.findByCartItemDTO(cartId, updateItem.getItemId());
            cartItem.updateCartStatus(CartStatus.PURCHASED);
            cartItemRepository.save(cartItem);

            //아이템 이미지 삭제처리
            List<ItemImgEntity> findImg = itemImgRepository.findByItemItemId(updateItem.getItemId());
            for (ItemImgEntity img : findImg) {
                String uploadFilePath = img.getUploadImgPath();
                String uuidFileName = img.getUploadImgName();

                // DB에서 이미지 삭제
                itemImgRepository.deleteById(img.getItemImgId());
                // S3에서 삭제
                String result = s3ItemImgUploaderService.deleteFile(uploadFilePath, uuidFileName);
                log.info(result);
            }
        }
        return savedOrder;
    }

    // 모든 문의글 보기
    @Transactional(readOnly = true)
    @Override
    public Page<BoardDTO> getAllBoards(Pageable pageable, String searchKeyword, UserDetails userDetails) {
        // userDetails에서 권한을 가져오기
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        String email = userDetails.getUsername();
        MemberEntity findEmail = memberRepository.findByEmail(email);
        log.info("관리자 정보 : " + findEmail);

        Page<BoardEntity> allBoards;
        // 권한이 있는지 체크
        if (!authorities.isEmpty()) {
            String role = authorities.iterator().next().getAuthority();
            log.info("권한 : " + role);
            // 관리자 권한 체크
            if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                // 키워드로 페이지 처리해서 검색
                allBoards = boardRepository.findByTitleContaining(pageable, searchKeyword);
                // 댓글이 존재하는지 아닌지 체크할 수 있게 상태를 바꿔줍니다.
                replyCheck(allBoards);

                // 관리자라 모두 읽을 수 있으니 UN_LOCK
                allBoards.forEach(board -> board.changeSecret(BoardSecret.UN_LOCK));
                return allBoards.map(board -> BoardDTO.toBoardDTO(
                        board,
                        board.getMember().getNickName(),
                        board.getItem().getItemId()));
            }
        }
        return null;
    }
    // 댓글이 존재하는지 아닌지 체크할 수 있게 상태를 바꿔줍니다.
    private static void replyCheck(Page<BoardEntity> allBoards) {
        // 댓글이 없으면 답변 미완료, 있으면 완료
        for(BoardEntity boardCheck : allBoards) {
            if(boardCheck.getCommentEntityList().isEmpty()) {
                boardCheck.changeReply(ReplyStatus.REPLY_X);
            } else {
                boardCheck.changeReply(ReplyStatus.REPLY_O);
            }
        }
    }


    // 관리자가 해당 유저의 모든 문의글 보기
    @Transactional(readOnly = true)
    @Override
    public Page<BoardDTO> getBoardsByNiickName(UserDetails userDetails,
                                               Pageable pageable,
                                               String nickName,
                                               String searchKeyword) {
        // userDetails에서 권한을 가져오기
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        // 현재는 권한이 1개만 있는 것으로 가정
        if (!authorities.isEmpty()) {
            String role = authorities.iterator().next().getAuthority();
            // 존재하는 권한이 관리자인지 체크
            if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                Page<BoardEntity> allByNickName = boardRepository.findByMemberNickNameAndTitleContaining(nickName, pageable, searchKeyword);
                // 댓글이 존재하는지 아닌지 체크할 수 있게 상태를 바꿔줍니다.
                replyCheck(allByNickName);
                // 관리자라 모두 읽을 수 있으니 UN_LOCK
                allByNickName.forEach(board -> board.changeSecret(BoardSecret.UN_LOCK));
                return allByNickName.map(board -> BoardDTO.toBoardDTO(
                        board,
                        nickName,
                        board.getItem().getItemId()));
            }
        }
        return null;
    }

    // 문의글 상세 보기
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<BoardDTO> getBoard(Long boardId, UserDetails userDetails) {
        try {
            // userDetails에서 권한을 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            // 문의글 조회
            BoardEntity findBoard = boardRepository.findByBoardId(boardId)
                    .orElseThrow(EntityNotFoundException::new);

            // 권한이 있는지 체크
            if (!authorities.isEmpty()) {
                String role = authorities.iterator().next().getAuthority();
                log.info("권한 : " + role);
                if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                    BoardDTO returnBoard = BoardDTO.toBoardDTO(
                            findBoard,
                            findBoard.getMember().getNickName(),
                            findBoard.getItem().getItemId());
                    return ResponseEntity.ok().body(returnBoard);
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 상품에 대한 문의글 보기
    @Transactional(readOnly = true)
    @Override
    public Page<BoardDTO> getItemBoards(Pageable pageable,
                                    Long itemId,
                                    UserDetails userDetails) {

        // 권한 조회
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String email = userDetails.getUsername();
        log.info("관리자 정보 : " + email);

        // 상품 조회
        ItemEntity findItem = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        log.info("상품 : " + findItem);

        Page<BoardEntity> allItemBoards;

        // 권한 체크
        if (!authorities.isEmpty()) {
            String authority = authorities.iterator().next().getAuthority();
            log.info("권한 : " + authority);
            if (authority.equals("ADMIN") || authority.equals("ROLE_ADMIN")) {
                allItemBoards = boardRepository.findAllByItemItemId(itemId, pageable);
                // 댓글이 존재하는지 아닌지 체크할 수 있게 상태를 바꿔줍니다.
                replyCheck(allItemBoards);

                allItemBoards.forEach(board -> board.changeSecret(BoardSecret.UN_LOCK));
                return allItemBoards.map(board -> BoardDTO.toBoardDTO(
                        board,
                        board.getMember().getNickName(),
                        board.getItem().getItemId()
                ));
            }
        }
        return null;
    }

}