package wms.orion.dto.inboundDTO.armazenagem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wms.orion.models.inventory.WarehouseInventory;

@Getter
@AllArgsConstructor
public class StorageResponseDTO {
    private String licensePlate;
    private String newLocation;
    private String status;
    private String sku;
    private Double quantidade;

    // Construtor de conveniência para converter a Entidade em DTO
    public StorageResponseDTO(WarehouseInventory inventory) {
        this.licensePlate = inventory.getLicensePlate();
        this.newLocation = inventory.getLocationCode();
        this.status = inventory.getStatus().toString();
        this.sku = inventory.getSku();
        this.quantidade = (double) inventory.getQuantity();
    }
}