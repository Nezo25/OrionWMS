CREATE TABLE IF NOT EXISTS wms_storage_location (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location_code VARCHAR(50) NOT NULL UNIQUE,
    zone VARCHAR(50),
    occupied BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed opcional: Criando alguns endereços para teste
INSERT INTO wms_storage_location (location_code, zone) VALUES
('R01-P01-N01', 'PALLET'),
('R01-P01-N02', 'PALLET'),
('R02-P01-N01', 'PALLET'),
('DOCK-01', 'RECEBIMENTO');