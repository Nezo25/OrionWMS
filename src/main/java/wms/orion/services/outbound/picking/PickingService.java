package wms.orion.services.outbound.picking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.orion.models.enums.OutboundStatus;
import wms.orion.models.inventory.WarehouseInventory;
import wms.orion.models.outbound.OutboundItem;
import wms.orion.models.outbound.OutboundOrder;
import wms.orion.models.repositories.InventoryRepository;
import wms.orion.models.repositories.OutboundOrderRepository;

import java.util.List;

@Service
public class PickingService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OutboundOrderRepository outboundOrderRepository;

    @Transactional
    public OutboundOrder criarPedidoEReservar(OutboundOrder order) {
        // Salva a ordem primeiro para gerar o ID
        OutboundOrder savedOrder = outboundOrderRepository.save(order);

        for (OutboundItem item : savedOrder.getItems()) {
            List<WarehouseInventory> estoques = inventoryRepository.findAvailableInventory(item.getSku());
            int saldoNecessario = item.getQuantityRequested();

            for (WarehouseInventory inv : estoques) {
                if (saldoNecessario <= 0) break;

                int disponivel = inv.getQuantity() - inv.getQuantityAllocated();
                int alocar = Math.min(disponivel, saldoNecessario);

                inv.setQuantityAllocated(inv.getQuantityAllocated() + alocar);
                saldoNecessario -= alocar;
                inventoryRepository.save(inv);
            }

            if (saldoNecessario > 0) {
                throw new RuntimeException("Estoque insuficiente para o SKU: " + item.getSku());
            }
        }

        savedOrder.setStatus(OutboundStatus.PICKING_PENDENTE);
        return outboundOrderRepository.save(savedOrder);
    }

    @Transactional
    public void confirmarColetaItem(Long orderId, String sku, int quantidadeColetada) {
        OutboundOrder order = outboundOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        OutboundItem itemPedido = order.getItems().stream()
                .filter(i -> i.getSku().equals(sku))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("SKU não pertence a este pedido"));

        WarehouseInventory estoque = inventoryRepository.findInventoryWithReservation(sku)
                .orElseThrow(() -> new RuntimeException("Nenhuma reserva encontrada para este SKU"));

        estoque.setQuantity(estoque.getQuantity() - quantidadeColetada);
        estoque.setQuantityAllocated(estoque.getQuantityAllocated() - quantidadeColetada);
        itemPedido.setQuantityPicked(itemPedido.getQuantityPicked() + quantidadeColetada);

        inventoryRepository.save(estoque);
        verificarStatusFinal(order);
    }

    @Transactional
    public String confirmarColetaERetornarProximo(Long orderId, String sku, int quantidadeColetada) {
        confirmarColetaItem(orderId, sku, quantidadeColetada);

        OutboundOrder order = outboundOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (order.getStatus() == OutboundStatus.CONCLUIDO) {
            return "Picking finalizado com sucesso! Dirija-se à área de expedição.";
        }

        return order.getItems().stream()
                .filter(item -> item.getQuantityPicked() < item.getQuantityRequested())
                .findFirst()
                .map(proximoItem -> {
                    WarehouseInventory proxEstoque = inventoryRepository.findInventoryWithReservation(proximoItem.getSku())
                            .orElseThrow(() -> new RuntimeException("Reserva não encontrada para o próximo item"));

                    return String.format("Item coletado! Agora dirija-se ao endereço [%s] para coletar %d unidades do SKU: %s",
                            proxEstoque.getLocationCode(),
                            (proximoItem.getQuantityRequested() - proximoItem.getQuantityPicked()),
                            proximoItem.getSku());
                })
                .orElse("Pedido em separação, mas não há itens pendentes mapeados.");
    }

    private void verificarStatusFinal(OutboundOrder order) {
        boolean tudoColetado = order.getItems().stream()
                .allMatch(i -> i.getQuantityPicked().equals(i.getQuantityRequested()));

        order.setStatus(tudoColetado ? OutboundStatus.CONCLUIDO : OutboundStatus.EM_SEPARACAO);
        outboundOrderRepository.save(order);
    }
}