package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xToOne (ManyToOne, OneToOne) Order Order -> Member Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;

	@GetMapping("/api/v2/simple-orders")
	public List<OrderSimpleQueryDto> ordersV2() {
		return orderRepository.findAllByCriteria(new OrderSearch())
			.stream().map(OrderSimpleQueryDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v3/simple-orders")
	public List<OrderSimpleQueryDto> ordersV3() {
		List<Order> orders = orderRepository.findAllWithMemberDelivery();
		return orders.stream()
			.map(OrderSimpleQueryDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4() {
		return orderSimpleQueryRepository.findOrderDtos();
	}

}
