package com.example.shopping.service.order;

import com.example.shopping.domain.member.Role;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;

import com.example.shopping.entity.member.MemberEntity;

import com.example.shopping.exception.member.UserException;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.repository.order.OrderItemRepository;
import com.example.shopping.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.ArrayList;
/*
 *   writer : 오현진
 *   work :
 *          주문 서비스
 *          이렇게 인터페이스를 만들고 상속해주는 방식을 선택한 이유는
 *          메소드에 의존하지 않고 필요한 기능만 사용할 수 있게 하고 가독성과 유지보수성을 높이기 위해서 입니다.
 *   date : 2023/11/06
 * */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements  OrderService{
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;

    //회원 주문조회
    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDTO> getOrders(String email, String user) {
        MemberEntity authUser = memberRepository.findByEmail(user);
        if(authUser == null)
            throw new UserException("유저가 등록되어 있지 않습니다.");

        //email로 order_id조회하여 판매된 내역 보여줌
        MemberEntity member = memberRepository.findByEmail(email);

        List<OrderDTO> orderList;
        List<OrderItemDTO> orderItemList = new ArrayList<>();

        if(member.getMemberRole() == Role.ADMIN) {
            orderList = orderRepository.findByOrderAdmin(member.getMemberId());
            for(OrderDTO order : orderList){
                List<OrderItemDTO> itemList = orderItemRepository.findByOrderOrderId(order.getOrderId());
                orderItemList.addAll(itemList);
            }
        }
        else{
            //구매자 기준 조회
            orderList = orderRepository.findByOrderMember(member.getMemberId());
            for(OrderDTO order : orderList) {
                List<OrderItemDTO> itemList = orderItemRepository.findByOrderOrderId(order.getOrderId());
                orderItemList.addAll(itemList);
            }
            //판매자 기준 조회
            List<OrderItemDTO> itemList = orderItemRepository.findByItemSeller(member.getMemberId());
            orderItemList.addAll(itemList);
        }
        return orderItemList;
    }

    //회원 주문내역 Page로 return
    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemDTO> getOrdersPage(Pageable pageable, String email) {
        //email로 order_id조회하여 판매된 내역 보여줌
        MemberEntity member = memberRepository.findByEmail(email);

        List<OrderDTO> orderList;
        List<OrderItemDTO> orderItemList = new ArrayList<>();

        //Pageable 값 셋팅 - List to Page
        Pageable pageRequest = createPageRequestUsing(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        if(member.getMemberRole() == Role.ADMIN) {
            orderList = orderRepository.findByOrderAdmin(member.getMemberId());
            for(OrderDTO order : orderList){
                List<OrderItemDTO> itemList = orderItemRepository.findByOrderOrderId(order.getOrderId());
                orderItemList.addAll(itemList);
            }
        }
        else{
            //구매자 기준 조회
            orderList = orderRepository.findByOrderMember(member.getMemberId());
            for(OrderDTO order : orderList) {
                List<OrderItemDTO> itemList = orderItemRepository.findByOrderOrderId(order.getOrderId());
                orderItemList.addAll(itemList);
            }
            //판매자 기준 조회
            List<OrderItemDTO> itemList = orderItemRepository.findByItemSeller(member.getMemberId());
            orderItemList.addAll(itemList);
        }

        if(orderItemList.isEmpty()){
            throw new EntityNotFoundException("구매한 내역이 없습니다.");
        }

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), orderItemList.size());

        List<OrderItemDTO> subOrderItem = orderItemList.subList(start, end);
        return new PageImpl<>(subOrderItem, pageRequest, orderItemList.size());
    }

    private Pageable createPageRequestUsing(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }

}
