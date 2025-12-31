package wms.orion.dto.recebimento;

public record ReceivingLineDTO(
        String sku,
        int quantidadeEsperada,
        String lote
) {}
