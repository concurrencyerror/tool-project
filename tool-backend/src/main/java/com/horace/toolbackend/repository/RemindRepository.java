package com.horace.toolbackend.repository;

import com.horace.toolbackend.entity.RemindEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemindRepository extends JpaRepository<RemindEntity, Long> {
}
