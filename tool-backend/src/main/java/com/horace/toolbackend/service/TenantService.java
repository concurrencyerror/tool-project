package com.horace.toolbackend.service;

import com.horace.toolbackend.entity.Tenant;
import com.horace.toolbackend.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    private static final Logger log = LoggerFactory.getLogger(TenantService.class);

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    //查询所有的租户
    public List<Tenant> findAll() {
        return tenantRepository.findAll();
    }

    //根据 id 查询相关租户
    public Tenant findById(String tenantId) {
        return tenantRepository.findByTenantId(tenantId);
    }

    //保存数据
    @Transactional(rollbackFor = Exception.class)
    public Tenant save(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    //批量保存
    @Transactional(rollbackFor = Exception.class)
    public void save(List<Tenant> tenants) {
        tenantRepository.saveAll(tenants);
    }

    //删除相关数据
    @Transactional(rollbackFor = Exception.class)
    public void delete(Tenant tenant) {
        tenantRepository.delete(tenant);
    }

    //根据 id 进行删除
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        tenantRepository.deleteById(id);
    }
}
