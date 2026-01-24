CREATE TABLE bs_user
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    username  VARCHAR(255)          NULL,
    password  VARCHAR(255)          NULL,
    tenant_id BIGINT                NULL,
    CONSTRAINT pk_bs_user PRIMARY KEY (id)
);

ALTER TABLE bs_user
    ADD CONSTRAINT FK_BS_USER_ON_TENANT FOREIGN KEY (tenant_id) REFERENCES bs_tenant (id);