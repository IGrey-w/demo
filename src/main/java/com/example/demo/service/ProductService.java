package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 分页查询所有上架商品
     */
    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return productRepository.findByStatus(1, pageable);
    }

    /**
     * 按分类分页查询
     */
    public Page<Product> getByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return productRepository.findByCategoryAndStatus(category, 1, pageable);
    }

    /**
     * 关键词搜索
     */
    public Page<Product> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return productRepository.searchByName(keyword, pageable);
    }

    /**
     * 按分类+关键词搜索
     */
    public Page<Product> searchByCategoryAndKeyword(String category, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return productRepository.findByCategoryAndNameLike(category, keyword, pageable);
    }

    /**
     * 根据ID查询商品详情
     */
    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * 获取所有分类（去重）
     */
    public List<String> getAllCategories() {
        return productRepository.findAll().stream()
                .map(Product::getCategory)
                .distinct()
                .filter(c -> c != null && !c.isEmpty())
                .toList();
    }

    /**
     * 新增商品（管理员）
     */
    @Transactional
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * 更新商品（管理员）
     */
    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setDescription(product.getDescription());
        existing.setImageUrl(product.getImageUrl());
        existing.setCategory(product.getCategory());
        existing.setStatus(product.getStatus());
        return productRepository.save(existing);
    }

    /**
     * 删除商品（管理员）
     */
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
