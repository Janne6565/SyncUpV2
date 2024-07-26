package com.janne.syncupv2.repository;

import com.janne.syncupv2.model.jpa.post.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {
}
