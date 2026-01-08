-- Tabela de Pedidos de Saída
CREATE TABLE outbound_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL -- PICKING_PENDENTE, EM_SEPARACAO, CONCLUIDO
);

-- Tabela de Itens do Pedido
CREATE TABLE outbound_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    sku VARCHAR(50) NOT NULL,
    quantity_requested INT NOT NULL,
    quantity_picked INT DEFAULT 0,
    CONSTRAINT fk_outbound_order FOREIGN KEY (order_id) REFERENCES outbound_order(id)
);