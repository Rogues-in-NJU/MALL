package edu.nju.mall.entity;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    protected Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    protected String createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    protected String updatedAt;

    @Column(name = "deleted_at")
    protected String deletedAt;

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(createdAt)) {
            // 格式yyyy-MM-dd HH:mm:ss
            createdAt = DateUtil.now();
        }
        if (Objects.isNull(updatedAt)) {
            // 格式yyyy-MM-dd HH:mm:s
            updatedAt = DateUtil.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (Objects.isNull(updatedAt)) {
            // 格式yyyy-MM-dd HH:mm:s
            updatedAt = DateUtil.now();
        }
    }

}
