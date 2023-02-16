package io.pi.spring_ecom.rest;

import io.pi.spring_ecom.domain.AppUser;
import io.pi.spring_ecom.domain.Order;
import io.pi.spring_ecom.model.OrderDTO;
import io.pi.spring_ecom.model.request.OrderRequest;
import io.pi.spring_ecom.model.request.SingleOrderRequest;
import io.pi.spring_ecom.model.responses.OrderResponse;
import io.pi.spring_ecom.repos.AppUserRepository;
import io.pi.spring_ecom.repos.OrderRepository;
import io.pi.spring_ecom.service.OrderService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;


@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderResource {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final AppUserRepository appUserRepository;

    public OrderResource(final OrderService orderService,
                         OrderRepository orderRepository,
                         AppUserRepository appUserRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }


    @GetMapping("/history/{id}")
    public ResponseEntity<List<OrderResponse>> getOrderHistory(@PathVariable(name = "id") final Long id) {
        AppUser client = appUserRepository.findById(id).get();
        //List<Order> orders = orderRepository.findAllByClient_Id(id);
        List<Order> orders = orderRepository.findAllByClient(client);
        List<OrderResponse> orderResponses = new ArrayList<>();

        for(Order order : orders){
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setProductName(order.getProduct().getName());
            orderResponse.setDate(order.getDate());
            orderResponse.setUnitPrice(order.getProduct().getPrice());
            orderResponse.setTotalPrice(order.getTotalPrice());
            orderResponse.setQuantity(order.getQuantity());
            orderResponses.add(orderResponse);
        }
        System.out.println(orderResponses);

        return ResponseEntity.ok(orderResponses);
    }




    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createOrder(@RequestBody @Valid final OrderDTO orderDTO) {
        return new ResponseEntity<>(orderService.create(orderDTO), HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createOrder(@RequestBody @Valid final OrderRequest orderRequest) {
        for(SingleOrderRequest singleOrderRequest : orderRequest.getOrders()){
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setClient(orderRequest.getClientId());
            orderDTO.setProduct(singleOrderRequest.getProductId());
            orderDTO.setQuantity(singleOrderRequest.getQuantity());
            orderDTO.setDate(LocalDate.now());
            orderDTO.setTotalPrice(singleOrderRequest.getTotalPrice());
            orderService.create(orderDTO);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrder(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final OrderDTO orderDTO) {
        orderService.update(id, orderDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrder(@PathVariable(name = "id") final Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
