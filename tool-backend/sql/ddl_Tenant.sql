CREATE TABLE bs_tenant
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255)          NULL,
    tenant_id VARCHAR(255)          NULL,
    CONSTRAINT pk_bs_tenant PRIMARY KEY (id)
);