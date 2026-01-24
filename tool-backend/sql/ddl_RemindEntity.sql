CREATE TABLE bs_remind_config
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    remind_start_time datetime              NULL,
    remind_end_time   datetime              NULL,
    create_time       datetime              NULL,
    tenant_id         BIGINT                NULL,
    user_id           BIGINT                NULL,
    CONSTRAINT pk_bs_remind_config PRIMARY KEY (id)
);

ALTER TABLE bs_remind_config
    ADD CONSTRAINT FK_BS_REMIND_CONFIG_ON_TENANT FOREIGN KEY (tenant_id) REFERENCES bs_tenant (id);

ALTER TABLE bs_remind_config
    ADD CONSTRAINT FK_BS_REMIND_CONFIG_ON_USER FOREIGN KEY (user_id) REFERENCES bs_user (id);