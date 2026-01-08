package wms.orion.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.orion.models.enums.StatusRecebimento;
import wms.orion.models.inventory.WarehouseInventory;

import java.util.Optional;
import java.util.List;

public interface InventoryRepository extends JpaRepository<WarehouseInventory, Long> {


    List<WarehouseInventory> findByStatusAndLocationCode(StatusRecebimento status, String locationCode);

    Optional<WarehouseInventory> findByLicensePlate(String licensePlate);

    List<WarehouseInventory> findByReceivingHeaderId(Long id);
    @Query("SELECT i FROM WarehouseInventory i WHERE i.sku = :sku AND (i.quantity - i.quantityAllocated) > 0 ORDER BY i.id ASC")
    List<WarehouseInventory> findAvailableInventory(@Param("sku") String sku);
    @Query("SELECT i FROM WarehouseInventory i WHERE i.sku = :sku AND i.quantityAllocated > 0")
    Optional<WarehouseInventory> findInventoryWithReservation(@Param("sku") String sku);

}