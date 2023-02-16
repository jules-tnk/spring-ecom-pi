package io.pi.spring_ecom.controller;

import io.pi.spring_ecom.model.AppUserDTO;
import io.pi.spring_ecom.service.AppUserService;
import io.pi.spring_ecom.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/appUsers")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(final AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("appUsers", appUserService.findAll());
        return "appUser/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("appUser") final AppUserDTO appUserDTO) {
        return "appUser/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("appUser") @Valid final AppUserDTO appUserDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && appUserService.emailExists(appUserDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.appUser.email");
        }
        if (bindingResult.hasErrors()) {
            return "appUser/add";
        }
        appUserService.create(appUserDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("appUser.create.success"));
        return "redirect:/appUsers";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("appUser", appUserService.get(id));
        return "appUser/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("appUser") @Valid final AppUserDTO appUserDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") &&
                !appUserService.get(id).getEmail().equalsIgnoreCase(appUserDTO.getEmail()) &&
                appUserService.emailExists(appUserDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.appUser.email");
        }
        if (bindingResult.hasErrors()) {
            return "appUser/edit";
        }
        appUserService.update(id, appUserDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("appUser.update.success"));
        return "redirect:/appUsers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = appUserService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            appUserService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("appUser.delete.success"));
        }
        return "redirect:/appUsers";
    }

}
