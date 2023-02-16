package io.pi.spring_ecom.service;

import io.pi.spring_ecom.domain.AppUser;
import io.pi.spring_ecom.model.AppUserDTO;
import io.pi.spring_ecom.repos.AppUserRepository;
import io.pi.spring_ecom.util.NotFoundException;
import io.pi.spring_ecom.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(final AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<AppUserDTO> findAll() {
        final List<AppUser> appUsers = appUserRepository.findAll(Sort.by("id"));
        return appUsers.stream()
                .map((appUser) -> mapToDTO(appUser, new AppUserDTO()))
                .collect(Collectors.toList());
    }

    public AppUserDTO get(final Long id) {
        return appUserRepository.findById(id)
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public AppUserDTO getByEmail(final String email) {
        return appUserRepository.findByEmailIgnoreCase(email)
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public boolean existsByEmail(final String email) {
        return appUserRepository.existsByEmailIgnoreCase(email);
    }

    public Long create(final AppUserDTO appUserDTO) {
        final AppUser appUser = new AppUser();
        mapToEntity(appUserDTO, appUser);
        return appUserRepository.save(appUser).getId();
    }

    public void update(final Long id, final AppUserDTO appUserDTO) {
        final AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(appUserDTO, appUser);
        appUserRepository.save(appUser);
    }

    public void delete(final Long id) {
        appUserRepository.deleteById(id);
    }

    private AppUserDTO mapToDTO(final AppUser appUser, final AppUserDTO appUserDTO) {
        appUserDTO.setId(appUser.getId());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setFirstName(appUser.getFirstName());
        appUserDTO.setLastName(appUser.getLastName());
        appUserDTO.setPhoneNumber(appUser.getPhoneNumber());
        appUserDTO.setPassword(appUser.getPassword());
        return appUserDTO;
    }

    private AppUser mapToEntity(final AppUserDTO appUserDTO, final AppUser appUser) {
        appUser.setEmail(appUserDTO.getEmail());
        appUser.setFirstName(appUserDTO.getFirstName());
        appUser.setLastName(appUserDTO.getLastName());
        appUser.setPhoneNumber(appUserDTO.getPhoneNumber());
        appUser.setPassword(appUserDTO.getPassword());
        return appUser;
    }

    public boolean emailExists(final String email) {
        return appUserRepository.existsByEmailIgnoreCase(email);
    }

    @Transactional
    public String getReferencedWarning(final Long id) {
        final AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (!appUser.getClientOrders().isEmpty()) {
            return WebUtils.getMessage("appUser.order.manyToOne.referenced", appUser.getClientOrders().iterator().next().getId());
        }
        return null;
    }

}
