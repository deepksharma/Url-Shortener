package com.URL_Shortner.Deepak.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 1000)
    private String longUrl;

    @Column(unique = true , nullable = false)
    private String shortCode;

    @Builder.Default
    private Long clickCount = 0L;

    @Column(updatable = false)
    private LocalDateTime createdAt ;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
