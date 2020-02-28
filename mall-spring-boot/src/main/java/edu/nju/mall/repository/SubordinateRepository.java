package edu.nju.mall.repository;

import edu.nju.mall.entity.Subordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubordinateRepository extends JpaRepository<Subordinate, Long> {

    List<Subordinate> findByUserId(final Long userId);

}
