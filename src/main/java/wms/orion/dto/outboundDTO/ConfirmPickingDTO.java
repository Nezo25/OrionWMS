package wms.orion.dto.outboundDTO;

public record ConfirmPickingDTO(
        Long orderId,
        String sku,
        int quantity
) {}