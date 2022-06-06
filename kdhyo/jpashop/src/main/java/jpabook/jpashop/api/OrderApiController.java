package jpabook.jpashop.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderRepository orderRepository;
	private final OrderQueryRepository orderQueryRepository;

	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2() {
		List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

		return orders.stream()
			.map(OrderDto::new)
			.collect(toList());
	}

	@GetMapping("/api/v3/orders")
	public List<OrderDto> ordersV3() {
		List<Order> orders = orderRepository.findAllWithItem();

		return orders.stream()
			.map(OrderDto::new)
			.collect(toList());
	}

	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> ordersV3Page(
		@RequestParam(value = "offset", defaultValue = "0") int offset,
		@RequestParam(value = "limit", defaultValue = "0") int limit) {
		List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

		return orders.stream()
			.map(OrderDto::new)
			.collect(toList());
	}

	@GetMapping("/api/v4/orders")
	public List<OrderQueryDto> ordersV4() {
		return orderQueryRepository.findOrderQueryDtos();
	}

	@GetMapping("/api/v5/orders")
	public List<OrderQueryDto> ordersV5() {
		return orderQueryRepository.findAllByDtoOptimization();
	}

	@GetMapping("/api/v6/orders")
	public List<OrderQueryDto> ordersV6() {
		List<OrderFlatDto> flats = orderQueryRepository.findAllByDtoFlat();

		return flats.stream()
			.collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
				mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
			)).entrySet().stream()
			.map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
			.collect(toList());
	}

	@Getter
	static class OrderDto {

		private final Long orderId;
		private final String name;
		private final LocalDateTime orderDate;
		private final OrderStatus orderStatus;
		private final Address address;
		private final List<OrderItemDto> orderItems;

		OrderDto(Order order) {
			this.orderId = order.getId();
			this.name = order.getMember().getName();
			this.orderDate = order.getOrderDate();
			this.orderStatus = order.getStatus();
			this.address = order.getDelivery().getAddress();
			this.orderItems = order.getOrderItems().stream()
				.map(OrderItemDto::new)
				.collect(toList());
		}
	}

	@Getter
	static class OrderItemDto {

		private final String itemName;
		private final int orderPrice;
		private final int count;

		OrderItemDto(OrderItem orderItem) {
			this.itemName = orderItem.getItem().getName();
			this.orderPrice = orderItem.getOrderPrice();
			this.count = orderItem.getCount();
		}
	}

}
