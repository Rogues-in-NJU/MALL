package edu.nju.mall.repository;

import edu.nju.mall.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {

    List<UserRole> findAllByUserId(final Long userId);

    UserRole findByUserIdAndRoleId(final Long userId, final Long roleId);

}
