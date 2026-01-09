-- Adicionando a coluna user_id nas tabelas existentes
ALTER TABLE wms_storage_location ADD COLUMN user_id BIGINT;
ALTER TABLE inb_receiving_header ADD COLUMN user_id BIGINT;
ALTER TABLE inb_receiving_line ADD COLUMN user_id BIGINT;
ALTER TABLE OutboundOrder ADD COLUMN user_id BIGINT;
ALTER TABLE OutboundItem ADD COLUMN user_id BIGINT;

-- Adicionando as Constraints de chave estrangeira
ALTER TABLE wms_storage_location ADD CONSTRAINT fk_storage_user FOREIGN KEY (user_id) REFERENCES tb_users(user_id);
ALTER TABLE inb_receiving_header ADD CONSTRAINT fk_receiving_user FOREIGN KEY (user_id) REFERENCES tb_users(user_id);