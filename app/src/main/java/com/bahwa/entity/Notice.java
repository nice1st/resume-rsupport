package com.bahwa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="app_notice")
@Data
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Column(nullable = false, length = 10000)
    private String contents;
    
    @Column(nullable = false, length = 50)
    private String writer;
    
    @Column(length = 50)
    private String modifiedWriter;

    @Column(nullable = false)
    private LocalDateTime periodStart;
    
    private LocalDateTime periodEnd;

    @Column()
    @CreatedDate
    private LocalDateTime createdDate;

    @Column()
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    @Builder.Default
    private Long views = 0L;
    
    @Column(name = "is_delete", nullable = false, length = 1)
    @ColumnDefault(value = "'N'")
    @Type(type = "yes_no")
    @Builder.Default
    private Boolean isDeleted = false;
}
