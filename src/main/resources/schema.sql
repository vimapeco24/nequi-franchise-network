-- Table for Franchises
CREATE TABLE IF NOT EXISTS public.franchises
(
    id BIGSERIAL    PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP
);

-- Table for Branches
CREATE TABLE IF NOT EXISTS public.branches
(
    id BIGSERIAL    PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE,
    franchise_id    BIGINT NOT NULL,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP,
    CONSTRAINT fk_branch_franchise
        FOREIGN KEY (franchise_id)
            REFERENCES franchises (id)
            ON DELETE CASCADE
);

-- Table for Products
CREATE TABLE IF NOT EXISTS public.products
(
    id BIGSERIAL    PRIMARY KEY,
    name            VARCHAR(50) NOT NULL,
    stock           INT         NOT NULL CHECK (stock >= 0),
    branch_id       BIGINT      NOT NULL,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP,
    CONSTRAINT fk_product_branch
        FOREIGN KEY (branch_id)
            REFERENCES branches (id)
            ON DELETE CASCADE
);


