package com.microservices.produc.service.service;

import com.microservices.produc.service.dto.ProductRequest;
import com.microservices.produc.service.dto.ProductResponse;
import com.microservices.produc.service.model.Product;
import com.microservices.produc.service.reposetory.ProductReposetory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductReposetory productReposetory;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                                  .name(productRequest.getName())
                                  .description(productRequest.getDescription())
                                  .price(productRequest.getPrice())
                                  .build();
        productReposetory.save(product);
        log.info("product {} is saved");
    }


    public List<ProductResponse> getAllProduct() {
        List<Product> products = productReposetory.findAll();
        return products.stream().map(product -> mapToProductResponse(product)).collect(Collectors.toList());

    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                       .id(product.getId())
                       .name(product.getName())
                       .description(product.getDescription())
                       .price(product.getPrice())
                       .build();
    }


}
