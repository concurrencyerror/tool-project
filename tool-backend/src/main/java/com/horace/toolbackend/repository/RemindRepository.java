package com.horace.toolbackend.repository;

import com.horace.toolbackend.entity.RemindEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RemindRepository extends JpaRepository<RemindEntity, Long> {


    @Query("select r from RemindEntity r where r.remindStartTime <= :t and r.remindEndTime >= :t")
    List<RemindEntity> findActiveAt(@Param("t") LocalDateTime date);
}
