package wms.orion.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wms.orion.models.outbound.OutboundOrder;

public interface OutboundOrderRepository extends JpaRepository<OutboundOrder, Long> {
}