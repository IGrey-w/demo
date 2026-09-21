package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 分页查询所有上架商品
     * GET /api/products?page=0&size=10
     */
    @GetMapping
    public Result<Page<Product>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(productService.getAllProducts(page, size));
    }

    /**
     * 按分类查询商品
     * GET /api/products/category/电子产品?page=0&size=10
     */
    @GetMapping("/category/{category}")
    public Result<Page<Product>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(productService.getByCategory(category, page, size));
    }

    /**
     * 关键词搜索商品
     * GET /api/products/search?keyword=手机&page=0&size=10
     */
    @GetMapping("/search")
    public Result<Page<Product>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(productService.search(keyword, page, size));
    }

    /**
     * 分类+关键词搜索
     * GET /api/products/search/category?category=电子产品&keyword=手机&page=0&size=10
     */
    @GetMapping("/search/category")
    public Result<Page<Product>> searchByCategory(
            @RequestParam String category,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(productService.searchByCategoryAndKeyword(category, keyword, page, size));
    }

    /**
     * 查询商品详情
     * GET /api/products/1
     */
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return productService.getById(id)
                .map(Result::success)
                .orElse(Result.error("商品不存在"));
    }

    /**
     * 获取所有商品分类
     * GET /api/products/categories
     */
    @GetMapping("/categories")
    public Result<List<String>> getCategories() {
        return Result.success(productService.getAllCategories());
    }

    // ==================== 管理员接口（需要 ROLE_ADMIN 权限）====================

    /**
     * 新增商品
     * POST /api/products
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Product> addProduct(@RequestBody Product product) {
        return Result.success(productService.addProduct(product));
    }

    /**
     * 更新商品
     * PUT /api/products/1
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return Result.success(productService.updateProduct(id, product));
    }

    /**
     * 删除商品
     * DELETE /api/products/1
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success("删除成功");
    }
}
