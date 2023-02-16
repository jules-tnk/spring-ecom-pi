package io.pi.spring_ecom.service;

import io.pi.spring_ecom.domain.Product;
import io.pi.spring_ecom.model.ProductDTO;
import io.pi.spring_ecom.repos.ProductRepository;
import io.pi.spring_ecom.util.NotFoundException;
import io.pi.spring_ecom.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("id"));
        return products.stream()
                .map((product) -> mapToDTO(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    public ProductDTO get(final Long id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return productRepository.save(product).getId();
    }

    public void update(final Long id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        productRepository.save(product);
    }

    public void delete(final Long id) {
        productRepository.deleteById(id);
    }

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImgUrl(product.getImgUrl());
        productDTO.setPrice(product.getPrice());
        return productDTO;
    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImgUrl(productDTO.getImgUrl());
        product.setPrice(productDTO.getPrice());
        return product;
    }

    @Transactional
    public String getReferencedWarning(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (!product.getProductOrders().isEmpty()) {
            return WebUtils.getMessage("product.order.manyToOne.referenced", product.getProductOrders().iterator().next().getId());
        }
        return null;
    }

}
