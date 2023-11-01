package com.example.shopping.service.order;

import com.example.shopping.domain.member.Role;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;

import com.example.shopping.entity.member.MemberEntity;

import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.repository.order.OrderItemRepository;
import com.example.shopping.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements  OrderService{

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final MemberRepository memberRepository;

    //회원 주문조회
    @Override
    public List<OrderItemDTO> getOrders(String email) {
        //email로 order_id조회하여 판매된 내역 보여줌
        MemberEntity member = memberRepository.findByEmail(email);

        List<OrderDTO> orderList;
        List<OrderItemDTO> orderItemList = new ArrayList<>();

        if(member.getMemberRole() == Role.ADMIN) {
            orderList = orderRepository.findByOrderAdmin(member.getMemberId());
            for(OrderDTO order : orderList){
                List<OrderItemDTO> itemList = orderItemRepository.findByOrderOrderId(order.getOrderId());
                for(OrderItemDTO item : itemList)
                {
                    orderItemList.add(item);
                }
            }
        }
        else{
            //구매자 기준 조회
            orderList = orderRepository.findByOrderMember(member.getMemberId());
            for(OrderDTO order : orderList) {
                List<OrderItemDTO> itemList = orderItemRepository.findByOrderOrderId(order.getOrderId());
                for(OrderItemDTO item : itemList)
                {
                    orderItemList.add(item);
                }            }
            //판매자 기준 조회
            List<OrderItemDTO> itemList = orderItemRepository.findByItemSeller(member.getMemberId());
            for(OrderItemDTO item : itemList)
            {
                orderItemList.add(item);
            }        }
        return orderItemList;
    }

    //회원 주문내역 Page로 return
    @Override
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
                for(OrderItemDTO item : itemList)
                {
                    orderItemList.add(item);
                }
            }
        }
        else{
            //구매자 기준 조회
            orderList = orderRepository.findByOrderMember(member.getMemberId());
            for(OrderDTO order : orderList) {
                List<OrderItemDTO> itemList = orderItemRepository.findByOrderOrderId(order.getOrderId());
                for(OrderItemDTO item : itemList)
                {
                    orderItemList.add(item);
                }
            }
            //판매자 기준 조회
            List<OrderItemDTO> itemList = orderItemRepository.findByItemSeller(member.getMemberId());
            for(OrderItemDTO item : itemList)
            {
                orderItemList.add(item);
            }
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
