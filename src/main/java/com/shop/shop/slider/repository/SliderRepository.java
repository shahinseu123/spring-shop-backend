package com.shop.shop.slider.repository;

import com.shop.shop.slider.entity.Slider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Long> {
    @Query("""
        SELECT c
        FROM Slider c
        WHERE (:query IS NULL OR :query = '' OR LOWER(c.title) LIKE CONCAT('%', LOWER(:query), '%'))
    """)
    Page<Slider> findAllSliders(@Param("query") String query, Pageable pageable);
}