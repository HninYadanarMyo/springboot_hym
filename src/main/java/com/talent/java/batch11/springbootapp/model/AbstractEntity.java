package com.talent.java.batch11.springbootapp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Data
@MappedSuperclass
public abstract class AbstractEntity {

    @CreationTimestamp
    @Column(nullable = true)
    private LocalDateTime createdAt;
    @Column(nullable = true)
    private String createdBy;

    @UpdateTimestamp
    @Column(nullable = true)
    private LocalDateTime updatedAt;
    @Column(nullable = true)
    private String updatedBy;

    private LocalDateTime deletedAt;
    private String deletedBy;

}
