package com.edu.flysixbackend.repository;

import com.edu.flysixbackend.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentRepository extends JpaRepository<Segment, Long> {
}
