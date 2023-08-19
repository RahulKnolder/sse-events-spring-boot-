package com.microservices.produc.service.reposetory;

import com.microservices.produc.service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReposetory extends MongoRepository<Product,String> {


}
