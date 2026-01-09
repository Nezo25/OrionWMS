-- 1. Criar a tabela de Roles
CREATE TABLE tb_roles (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Criar a tabela de Usuários
CREATE TABLE tb_users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- 3. Criar a tabela associativa (Many-to-Many)
CREATE TABLE tb_users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES tb_roles(role_id) ON DELETE CASCADE
);

-- 4. Inserir as Roles iniciais conforme o seu Enum
INSERT INTO tb_roles (role_id, name) VALUES (1, 'ADMIN');
INSERT INTO tb_roles (role_id, name) VALUES (2, 'BASIC');