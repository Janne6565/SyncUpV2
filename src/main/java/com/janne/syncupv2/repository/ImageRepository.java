package com.janne.syncupv2.repository;

import com.janne.syncupv2.model.jpa.util.ScaledImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ScaledImage, Long> {
}
