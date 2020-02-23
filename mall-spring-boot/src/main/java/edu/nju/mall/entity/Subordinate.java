package edu.nju.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@Entity
@Table(name = "subordinate")
@NoArgsConstructor
@AllArgsConstructor
public class Subordinate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    /**
     * 下级用户id
     * */
    @Column(name = "subordinate_id")
    private Long subordinateId;

}
