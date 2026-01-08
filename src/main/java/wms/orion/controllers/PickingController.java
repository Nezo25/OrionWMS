package wms.orion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.orion.dto.outboundDTO.ConfirmPickingDTO;
import wms.orion.dto.outboundDTO.OutboundOrderDTO;
import wms.orion.models.enums.OutboundStatus;
import wms.orion.models.outbound.OutboundItem;
import wms.orion.models.outbound.OutboundOrder;
import wms.orion.services.outbound.picking.PickingService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wms/outbound/picking")
public class PickingController {

    @Autowired
    private PickingService pickingService;

    /**
     * Passo 1: Cria o pedido e reserva o estoque automaticamente.
     */
    @PostMapping("/reservar")
    public ResponseEntity<OutboundOrder> criarPedidoEReservar(@RequestBody OutboundOrderDTO dto) {
        OutboundOrder order = new OutboundOrder();
        order.setOrderNumber(dto.orderNumber());
        order.setStatus(OutboundStatus.PICKING_PENDENTE);

        List<OutboundItem> items = dto.items().stream().map(itemDto -> {
            OutboundItem item = new OutboundItem();
            item.setSku(itemDto.getSku());
            item.setQuantityRequested(itemDto.getQuantityRequested());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);

        OutboundOrder orderCriada = pickingService.criarPedidoEReservar(order);
        return ResponseEntity.status(201).body(orderCriada);
    }

    /**
     * Passo 2: Confirma a coleta do item e indica o próximo destino.
     */
    @PostMapping("/separação")
    public ResponseEntity<String> confirmarItem(@RequestBody ConfirmPickingDTO dto) {
        // Chamando o método do Service que processa a baixa e busca o próximo endereço
        String instrucaoProximoPasso = pickingService.confirmarColetaERetornarProximo(dto.orderId(), dto.sku(), dto.quantity());

        return ResponseEntity.ok("Item coletado com sucesso! Próxima instrução: " + instrucaoProximoPasso);
    }
}