package com.example.shopping.controller.member;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.jwt.TokenDTO;
import com.example.shopping.domain.member.LoginDTO;
import com.example.shopping.domain.member.RequestMemberDTO;
import com.example.shopping.domain.member.ResponseMemberDTO;
import com.example.shopping.domain.member.ModifyMemberDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.service.board.BoardServiceImpl;
import com.example.shopping.service.jwt.TokenServiceImpl;
import com.example.shopping.service.member.MemberServiceImpl;
import com.example.shopping.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
// @Slf4j를 사용하지 않고 Log4j2를 사용하는 이유는
// 기능면에서 더 좋기 때문입니다.
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "member", description = "유저 API")
public class MemberController {

    private final MemberServiceImpl memberServiceImpl;
    private final TokenServiceImpl tokenServiceImpl;
    private final OrderService orderService;
    private final BoardServiceImpl boardService;

    // 회원가입
    @PostMapping("/")
    @Tag(name = "member")
    @Operation(summary = "회원가입", description = "회원가입하는 API입니다")
    // BindingResult 타입의 매개변수를 지정하면 BindingResult 매개 변수가 입력값 검증 예외를 처리한다.
    public ResponseEntity<?> join(@Validated @RequestBody RequestMemberDTO member,
                                  BindingResult result) {
        try {
            // 입력값 검증 예외가 발생하면 예외 메시지를 응답한다.
            if (result.hasErrors()) {
                log.info("BindingResult error : " + result.hasErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getClass().getSimpleName());
            }

            ResponseEntity<?> signUp = memberServiceImpl.signUp(member);
            return ResponseEntity.ok().body(signUp);
        } catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 회원 조회
    @GetMapping("/{memberId}")
    @Tag(name = "member")
    @Operation(summary = "회원 조회", description = "회원을 검색하는 API입니다.")
    public ResponseEntity<ResponseMemberDTO> search(@PathVariable Long memberId) {
        try {
            ResponseMemberDTO search = memberServiceImpl.search(memberId);
            return ResponseEntity.ok().body(search);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/{memberId}")
    @Tag(name = "member")
    @Operation(summary = "삭제 API", description = "유저를 삭제하는 API입니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String remove(@PathVariable Long memberId,
                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            String remove = memberServiceImpl.removeUser(memberId, email);
            return remove;
        } catch (Exception e) {
            return "회원탈퇴 실패했습니다.";
        }
    }

    // 로그인
    @PostMapping("/login")
    @Tag(name = "member")
    @Operation(summary = "로그인 API", description = "로그인을 하면 JWT를 반환해줍니다.")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            String email = loginDTO.getMemberEmail();
            String password = loginDTO.getMemberPw();
            ResponseEntity<?> login = memberServiceImpl.login(email, password);
            return ResponseEntity.ok().body(login);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 실패했습니다.");
        }
    }

    // 회원 수정
    @PutMapping("/{memberId}")
    @Tag(name = "member")
    @Operation(summary = "수정 API", description = "유저 정보를 수정하는 API입니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long memberId,
                                    @RequestBody ModifyMemberDTO modifyMemberDTO,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            ResponseEntity<?> responseEntity = memberServiceImpl.updateUser(memberId, modifyMemberDTO, email);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("잘못된 요청");
        }
    }

    // 중복체크
    @GetMapping("/email/{memberEmail}")
    @Tag(name = "member")
    @Operation(summary = "중복체크 API", description = "userEmail이 중복인지 체크하는 API입니다.")
    public String emailCheck(@PathVariable String memberEmail) {
        log.info("email : " + memberEmail);
        String result = memberServiceImpl.emailCheck(memberEmail);
        return result;
    }

    // accessToken 만료시 refreshToken으로 accessToken 발급
    @GetMapping("/refresh")
    @Tag(name = "member")
    @Operation(summary = "access token 발급", description = "refresh token을 받으면 access token을 반환해줍니다.")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) throws Exception {
        try {
            if (token != null) {
                ResponseEntity<TokenDTO> accessToken = tokenServiceImpl.createAccessToken(token);
                return ResponseEntity.ok().body(accessToken);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 닉네임 조회
    @GetMapping("/nickName/{nickName}")
    @Tag(name = "member")
    @Operation(summary = "닉네임 조회", description = "중복된 닉네임이 있는지 확인하는 API입니다.")
    public String nickNameCheck(@PathVariable String nickName) {
        log.info("nickName : " + nickName);
        String result = memberServiceImpl.nickNameCheck(nickName);
        return result;
    }

    // 주문 조회
    @GetMapping(value = "/order/{findEmail}")
    @Tag(name = "member")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "주문내역조회", description = "주문내역을 조회하는 API입니다.")
    public ResponseEntity<?> getOrders(@PathVariable String findEmail
            ,@AuthenticationPrincipal UserDetails userDetails) {
        List<OrderItemDTO> orders = new ArrayList<>();
        try {
            String email = userDetails.getUsername();
            orders = orderService.getOrders(findEmail, email);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(orders);
    }

    // 나의 문의글 확인
    @GetMapping("/myboards")
    @Tag(name = "member")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "문의글 보기", description = "자신이 문의한 게시글을 보는 API입니다.")
    public ResponseEntity<?> getBoards(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(sort = "boardId", direction = Sort.Direction.DESC)
            Pageable pageable,
            String searchKeyword) {
        try {
            String email = userDetails.getUsername();
            log.info("유저 : " + email);

            Page<BoardDTO> boards = boardService.getBoards(email, pageable, searchKeyword);
            Map<String, Object> response = new HashMap<>();
            // 현재 페이지의 아이템 목록
            response.put("items", boards.getContent());
            // 현재 페이지 번호
            response.put("nowPageNumber", boards.getNumber());
            // 전체 페이지 수
            response.put("totalPage", boards.getTotalPages());
            // 한 페이지에 출력되는 데이터 개수
            response.put("pageSize", boards.getSize());
            // 다음 페이지 존재 여부
            response.put("hasNextPage", boards.hasNext());
            // 이전 페이지 존재 여부
            response.put("hasPreviousPage", boards.hasPrevious());
            // 첫 번째 페이지 여부
            response.put("isFirstPage", boards.isFirst());
            // 마지막 페이지 여부
            response.put("isLastPage", boards.isLast());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
