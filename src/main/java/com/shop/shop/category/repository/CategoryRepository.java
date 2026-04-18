package com.shop.shop.category.repository;

import com.shop.shop.category.entity.Category;
import com.shop.shop.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {



    @Query("""
        SELECT c.id as id, 
               c.name as name, 
               c.slug as slug, 
               c.imageUrl as imageUrl,
               c.createdAt as createdAt
        FROM Category c
        WHERE(:query IS NULL  OR :query = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', LOWER(:query), '%')))

    """)
    List<CategoryProjection> findAllCategories(@Param("query") String query);

    @Query("""
    SELECT c.id as id, 
           c.name as name, 
           c.slug as slug, 
           c.imageUrl as imageUrl,
           c.createdAt as createdAt,
           c.updatedAt as updatedAt
    FROM Category c
    WHERE c.id = :id
""")
    CategoryProjection findCategoryDetails(@Param("id") Long id);

    @Query("""
        SELECT sc.id as id, 
               sc.name as name, 
               sc.slug as slug, 
               sc.imageUrl as imageUrl,
               sc.createdAt as createdAt
        FROM Category sc
        WHERE sc.parent.id = :parentId
    """)
    List<CategoryProjection> findSubCategoriesByParentId(@Param("parentId") Long parentId);

    @Query("""
        SELECT p.id as id, 
               p.name as name
        FROM Product p
        WHERE p.category.id = :categoryId
    """)
    List<ProductProjection> findProductsByCategoryId(@Param("categoryId") Long categoryId);

    interface CategoryProjection {
        Long getId();
        String getName();
        String getSlug();
        String getImageUrl();
        LocalDateTime getCreatedAt();
    }

    interface ProductProjection {
        Long getId();
        String getName();
    }



//    @Query(value = """
//    SELECT
//        c.id,
//        c.name,
//        c.slug,
//        c.image_url as imageUrl,
//        c.created_at as createdAt,
//        c.updated_at as updatedAt,
//        COALESCE(
//            (
//                SELECT JSONB_AGG(
//                    JSONB_BUILD_OBJECT(
//                        'id', sc.id,
//                        'name', sc.name,
//                        'slug', sc.slug,
//                        'imageUrl', sc.image_url,
//                        'createdAt', sc.created_at
//                    )
//                    ORDER BY sc.id
//                )
//                FROM categories sc
//                WHERE sc.parent_id = c.id
//            ),
//            '[]'::jsonb
//        ) as subCategories,
//        COALESCE(
//            (
//                SELECT JSONB_AGG(
//                    JSONB_BUILD_OBJECT(
//                        'id', p.id,
//                        'name', p.name,
//                        'price', p.price,
//                        'imageUrl', p.image_url
//                    )
//                    ORDER BY p.id
//                )
//                FROM products p
//                WHERE p.category_id = c.id
//            ),
//            '[]'::jsonb
//        ) as products
//    FROM categories c
//    WHERE c.id = :id
//""", nativeQuery = true)
//    CategoryDetailsNativeProjection findCategoryDetailsNative(@Param("id") Long id);

}
