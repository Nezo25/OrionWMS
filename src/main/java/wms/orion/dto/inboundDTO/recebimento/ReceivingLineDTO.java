package wms.orion.dto.inboundDTO.recebimento;

public record ReceivingLineDTO(
        String sku,
        int quantidadeEsperada,
        String lote
) {}
