package com.horace.toolbackend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "bs_remind_config")
public class RemindEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    //提醒的开始时间
    private Date remindStartTime;

    //提醒的结束时间
    private Date remindEndTime;

    //配置的开始时间
    private Date createTime;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getRemindStartTime() {
        return remindStartTime;
    }

    public void setRemindStartTime(Date remindStartTime) {
        this.remindStartTime = remindStartTime;
    }

    public Date getRemindEndTime() {
        return remindEndTime;
    }

    public void setRemindEndTime(Date remindEndTime) {
        this.remindEndTime = remindEndTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
