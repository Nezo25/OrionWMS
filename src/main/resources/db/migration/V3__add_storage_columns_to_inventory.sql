-- Garante que a coluna de localização suporte o código do endereço
ALTER TABLE wms_inventory
MODIFY COLUMN location_code VARCHAR(50);

-- Adiciona um índice para busca rápida por License Plate, essencial para performance
CREATE INDEX idx_inventory_lp ON wms_inventory (license_plate);