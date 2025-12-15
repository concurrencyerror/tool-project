package com.horace.toolbackend.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bs_tenant")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //tenant 的名称
    private String name;

    //tenant 本来的id
    private String tenantId;

    @OneToMany(mappedBy = "tenant")
    private List<User> userList = new ArrayList<>();

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(User user) {
        userList.add(user);
        user.setTenant(this);
    }

    public void removeUser(User user) {
        userList.remove(user);
        user.setTenant(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
