package wms.orion.dto.outboundDTO;

import wms.orion.models.outbound.OutboundItem;

import java.util.List;

public record OutboundOrderDTO(
        String orderNumber,
        List<OutboundItem> items
) {
}
