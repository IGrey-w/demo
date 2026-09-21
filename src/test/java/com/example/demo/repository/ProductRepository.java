package com.example.demo.repository;


import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 按分类查询（上架商品）
    Page<Product> findByCategoryAndStatus(String category, Integer status, Pageable pageable);

    // 查询所有上架商品
    Page<Product> findByStatus(Integer status, Pageable pageable);

    // 关键词搜索（模糊匹配名称）
    @Query("SELECT p FROM Product p WHERE p.status = 1 AND p.name LIKE %:keyword%")
    Page<Product> searchByName(@Param("keyword") String keyword, Pageable pageable);

    // 按分类和关键词搜索
    @Query("SELECT p FROM Product p WHERE p.status = 1 AND p.category = :category AND p.name LIKE %:keyword%")
    Page<Product> findByCategoryAndNameLike(@Param("category") String category,
                                            @Param("keyword") String keyword,
                                            Pageable pageable);
}
