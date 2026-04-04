package com.horace.toolbackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bs_remind_config")
public class RemindEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //提醒的开始时间
    private LocalDateTime remindStartTime;

    //提醒的结束时间
    private LocalDateTime remindEndTime;

    //配置的开始时间
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRemindStartTime() {
        return remindStartTime;
    }

    public void setRemindStartTime(LocalDateTime remindStartTime) {
        this.remindStartTime = remindStartTime;
    }

    public LocalDateTime getRemindEndTime() {
        return remindEndTime;
    }

    public void setRemindEndTime(LocalDateTime remindEndTime) {
        this.remindEndTime = remindEndTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
