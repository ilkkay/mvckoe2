package translateit2.service;

import translateit2.persistence.model.Product;

public interface ProductService {
    Iterable<Product> listAllProducts();

    Product getProductById(Integer id);

    Product saveProduct(Product product);
    
    void deleteProduct(Integer id);
}
