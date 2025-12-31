package wms.orion.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wms.orion.models.enums.StatusRecebimento;
import wms.orion.models.inventory.WarehouseInventory;

import java.util.Optional;
import java.util.List;

public interface InventoryRepository extends JpaRepository<WarehouseInventory, Long> {

    List<WarehouseInventory> findByStatusAndLocationCode(StatusRecebimento status, String locationCode);

    Optional<WarehouseInventory> findByLicensePlate(String licensePlate);

    List<WarehouseInventory> findByReceivingHeaderId(Long id);
}