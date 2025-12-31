package wms.orion.models.inventory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wms.orion.models.enums.StatusRecebimento;
@Entity
@Table(name = "wms_inventory")
@Getter
@Setter
public class WarehouseInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String lote;
    private String licensePlate;
    private String locationCode;
    private int quantity;
    private int quantityAllocated = 0;
    private Long receivingHeaderId;

    @Enumerated(EnumType.STRING)
    private StatusRecebimento status;
}