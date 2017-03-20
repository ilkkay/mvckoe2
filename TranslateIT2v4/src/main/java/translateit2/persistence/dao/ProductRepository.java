package translateit2.persistence.dao;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
}
