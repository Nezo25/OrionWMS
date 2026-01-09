package wms.orion.models.repositories; // Ajuste o pacote conforme o seu projeto

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wms.orion.models.inbound.recebimento.ReceivingLine;

@Repository
public interface ReceivingLineRepository extends JpaRepository<ReceivingLine, Long> {

    // Futuramente implantar buscar linhas por SKU ou por status.
}