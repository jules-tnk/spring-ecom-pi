package io.pi.spring_ecom;

import io.pi.spring_ecom.domain.AppUser;
import io.pi.spring_ecom.domain.Product;
import io.pi.spring_ecom.repos.AppUserRepository;
import io.pi.spring_ecom.repos.ProductRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository,
                           AppUserRepository appUserRepository) {
        this.productRepository = productRepository;
        this.appUserRepository = appUserRepository;
    }

    private Boolean alreadySetup = false;
    private final AppUserRepository appUserRepository;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {return;}

        for (int i = 1; i < 20; i++) {
            Product product = new Product();
            product.setName("Product " + i);
            product.setPrice(100.0 * i);
            product.setDescription("Description " + i);
            product.setImgUrl(
                    "https://images.unsplash.com/photo-1481349518771-20055b2a7b24?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NHx8cmFuZG9tfGVufDB8fDB8fA%3D%3D&w=1000&q=80");
            productRepository.save(product);
        }


        for (int i = 0; i < 5; i++) {
            AppUser appUser = new AppUser();
            appUser.setFirstName("First Name " + i);
            appUser.setLastName("Last Name " + i);
            appUser.setEmail("email" + i + "@gmail.com");
            appUser.setPassword("password" + i);
            appUserRepository.save(appUser);
        }

        alreadySetup = true;
    }
}
