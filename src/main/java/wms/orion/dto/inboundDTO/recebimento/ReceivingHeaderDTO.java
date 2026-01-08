package wms.orion.dto.inboundDTO.recebimento;

import java.time.LocalDateTime;
import java.util.List;

public record ReceivingHeaderDTO(
        String numeroNotaFiscal,
        LocalDateTime dataChegada,
        String fornecedorCnpj,
        List<ReceivingLineDTO> lines) {
}
