package io.pi.spring_ecom.service;

import io.pi.spring_ecom.domain.AppUser;
import io.pi.spring_ecom.domain.Order;
import io.pi.spring_ecom.domain.Product;
import io.pi.spring_ecom.model.OrderDTO;
import io.pi.spring_ecom.repos.AppUserRepository;
import io.pi.spring_ecom.repos.OrderRepository;
import io.pi.spring_ecom.repos.ProductRepository;
import io.pi.spring_ecom.util.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;

    public OrderService(final OrderRepository orderRepository,
            final AppUserRepository appUserRepository, final ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.appUserRepository = appUserRepository;
        this.productRepository = productRepository;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("id"));
        return orders.stream()
                .map((order) -> mapToDTO(order, new OrderDTO()))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> findAllByClient(final Long id) {
        final List<Order> orders = orderRepository.findAllByClient_Id(id);
        return orders.stream()
                .map((order) -> mapToDTO(order, new OrderDTO()))
                .collect(Collectors.toList());
    }

    public OrderDTO get(final Long id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OrderDTO orderDTO) {
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getId();
    }

    public void update(final Long id, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }

    public void delete(final Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setId(order.getId());
        orderDTO.setDate(order.getDate());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setClient(order.getClient() == null ? null : order.getClient().getId());
        orderDTO.setProduct(order.getProduct() == null ? null : order.getProduct().getId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        return orderDTO;
    }

    private Order mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setDate(orderDTO.getDate());
        order.setQuantity(orderDTO.getQuantity());
        order.setTotalPrice(orderDTO.getTotalPrice());
        final AppUser client = orderDTO.getClient() == null ? null : appUserRepository.findById(orderDTO.getClient())
                .orElseThrow(() -> new NotFoundException("client not found"));
        order.setClient(client);
        final Product product = orderDTO.getProduct() == null ? null : productRepository.findById(orderDTO.getProduct())
                .orElseThrow(() -> new NotFoundException("product not found"));
        order.setProduct(product);
        return order;
    }

}
