package wms.orion.services.armazenagem;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.orion.models.enums.StatusRecebimento;
import wms.orion.models.inventory.WarehouseInventory;
import wms.orion.models.repositories.InventoryRepository;

@Service
public class StorageService {

    private final InventoryRepository inventoryRepository;

    public StorageService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public WarehouseInventory realizarArmazenagem(String licensePlate, String enderecoDestino) {
        // 1. Busca o pallet pelo License Plate (LP)
        WarehouseInventory inventory = inventoryRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new RuntimeException("License Plate " + licensePlate + " não localizada."));

        // 2. AJUSTE DE REGRA: Incluído 'ESTOQUE_AREA_RECEBIMENTO' para destravar o Putaway
        if (inventory.getStatus() != StatusRecebimento.RECEBIMENTO_CONCLUIDO &&
                inventory.getStatus() != StatusRecebimento.RECEBIDO &&
                inventory.getStatus() != StatusRecebimento.ESTOQUE_AREA_RECEBIMENTO) {

            throw new RuntimeException("Operação negada: O pallet " + licensePlate +
                    " está com status: " + inventory.getStatus());
        }

        // 3. Atualiza local físico e status para disponibilidade no estoque
        inventory.setLocationCode(enderecoDestino);
        inventory.setStatus(StatusRecebimento.ESTOQUE_DISPONIVEL);

        return inventoryRepository.save(inventory);
    }

    public WarehouseInventory buscarPalletPorLp(String lp) {
        return inventoryRepository.findByLicensePlate(lp)
                .orElseThrow(() -> new RuntimeException("Pallet " + lp + " não encontrado."));
    }

    @Transactional
    public void excluirPallet(String lp) {
        WarehouseInventory inventory = buscarPalletPorLp(lp);

        // 4. Trava de segurança para processos de saída
        if (inventory.getStatus().name().contains("EXPEDICAO") ||
                inventory.getStatus().name().contains("PICKING")) {
            throw new RuntimeException("Proibido excluir: O pallet já está em processo de saída/expedição.");
        }

        inventoryRepository.delete(inventory);
    }
}