package wms.orion.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wms.orion.models.inbound.recebimento.ReceivingHeader;

import java.util.Optional;
public interface ReceivingHeaderRepository extends JpaRepository<ReceivingHeader, Long> {

    Optional<ReceivingHeader> findByNumeroNotaFiscal(String numeroNotaFiscal);
}

