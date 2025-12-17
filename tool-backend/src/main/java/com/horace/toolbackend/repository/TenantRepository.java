package com.horace.toolbackend.repository;

import com.horace.toolbackend.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Tenant findByTenantId(String tenantId);
}
