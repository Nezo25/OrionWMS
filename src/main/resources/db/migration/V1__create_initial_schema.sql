-- 1. Tabela de Cabeçalho de Recebimento
CREATE TABLE inb_receiving_header (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_nota_fiscal VARCHAR(255) NOT NULL UNIQUE,
    data_chegada DATETIME,
    status VARCHAR(50) NOT NULL
);

-- 2. Tabela de Linhas de Recebimento (Itens esperados)
CREATE TABLE inb_receiving_line (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    header_id BIGINT NOT NULL,
    sku VARCHAR(100) NOT NULL,
    quantidade_esperada INT NOT NULL,
    quantidade_recebida INT DEFAULT 0,
    lote VARCHAR(100),
    status VARCHAR(50),
    CONSTRAINT fk_receiving_header FOREIGN KEY (header_id) REFERENCES inb_receiving_header(id)
);

-- 3. Tabela de Inventário (Estoque Físico e Pallets)
CREATE TABLE wms_inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(100) NOT NULL,
    sku VARCHAR(100) NOT NULL,
    lote VARCHAR(100),
    location_code VARCHAR(100),
    quantity INT NOT NULL,
    quantity_allocated INT DEFAULT 0,
    receiving_header_id BIGINT,
    status VARCHAR(50) NOT NULL
);