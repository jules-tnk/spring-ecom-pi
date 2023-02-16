package io.pi.spring_ecom.controller;

import io.pi.spring_ecom.domain.AppUser;
import io.pi.spring_ecom.domain.Product;
import io.pi.spring_ecom.model.OrderDTO;
import io.pi.spring_ecom.repos.AppUserRepository;
import io.pi.spring_ecom.repos.ProductRepository;
import io.pi.spring_ecom.service.OrderService;
import io.pi.spring_ecom.util.WebUtils;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;

    public OrderController(final OrderService orderService,
            final AppUserRepository appUserRepository, final ProductRepository productRepository) {
        this.orderService = orderService;
        this.appUserRepository = appUserRepository;
        this.productRepository = productRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("clientValues", appUserRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(AppUser::getId, AppUser::getEmail)));
        model.addAttribute("productValues", productRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(Product::getId, Product::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "order/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("order") final OrderDTO orderDTO) {
        return "order/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("order") @Valid final OrderDTO orderDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "order/add";
        }
        orderService.create(orderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("order.create.success"));
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("order", orderService.get(id));
        return "order/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("order") @Valid final OrderDTO orderDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "order/edit";
        }
        orderService.update(id, orderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("order.update.success"));
        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        orderService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("order.delete.success"));
        return "redirect:/orders";
    }

}
