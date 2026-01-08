package wms.orion.services.inboud.recebimento;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.orion.models.enums.StatusRecebimento;
import wms.orion.models.inbound.recebimento.ReceivingHeader;
import wms.orion.models.inbound.recebimento.ReceivingLine;
import wms.orion.models.inventory.WarehouseInventory;
import wms.orion.models.repositories.InventoryRepository;
import wms.orion.models.repositories.ReceivingHeaderRepository;
import wms.orion.models.repositories.ReceivingLineRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReceivingService {

    private final ReceivingHeaderRepository headerRepository;
    private final ReceivingLineRepository lineRepository;
    private final InventoryRepository inventoryRepository;

    public ReceivingService(ReceivingHeaderRepository headerRepository,
                            ReceivingLineRepository lineRepository,
                            InventoryRepository inventoryRepository) {
        this.headerRepository = headerRepository;
        this.lineRepository = lineRepository;
        this.inventoryRepository = inventoryRepository;
    }

    private String generateLicensePlate() {
        return "LP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Transactional
    public String registrarConferencia(String numeroNF, String sku, int quantidade, String lote) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero.");

        ReceivingHeader header = headerRepository.findByNumeroNotaFiscal(numeroNF)
                .orElseThrow(() -> new RuntimeException("NF não encontrada."));

        // Busca a linha correspondente ao SKU
        ReceivingLine line = header.getReceivingLines().stream()
                .filter(l -> l.getSku().equals(sku))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("SKU não pertence a esta NF."));

        // Validação de excesso
        int totalAposLeitura = line.getQuantidadeRecebida() + quantidade;
        if (totalAposLeitura > line.getQuantidadeEsperada()) {
            throw new RuntimeException("Excesso! Falta receber: " + (line.getQuantidadeEsperada() - line.getQuantidadeRecebida()));
        }

        // --- REGRA: NOVO PALLET EXCLUSIVO PARA CADA CONFERÊNCIA ---
        String lpFinal = generateLicensePlate();

        // 1. Atualiza a linha da NF com a quantidade e o LP gerado
        line.setQuantidadeRecebida(totalAposLeitura);
        line.setLote(lote);
        line.setLicensePlate(lpFinal);
        lineRepository.save(line);

        // 2. Cria o registro de inventário físico na Doca
        WarehouseInventory inventory = new WarehouseInventory();
        inventory.setLicensePlate(lpFinal);
        inventory.setSku(sku);
        inventory.setLote(lote);
        inventory.setQuantity(quantidade); // Atribuição de int
        inventory.setReceivingHeaderId(header.getId());
        inventory.setLocationCode("DOCK-01");
        inventory.setStatus(StatusRecebimento.ESTOQUE_AREA_RECEBIMENTO);
        inventoryRepository.save(inventory);

        verificarEFinalizarAutomaticamente(header);

        return lpFinal;
    }

    private void verificarEFinalizarAutomaticamente(ReceivingHeader header) {
        boolean concluido = header.getReceivingLines().stream()
                .allMatch(l -> l.getQuantidadeRecebida() == l.getQuantidadeEsperada());
        if (concluido) {
            header.setStatus(StatusRecebimento.RECEBIMENTO_CONCLUIDO);
            headerRepository.save(header);
        }
    }

    @Transactional
    public ReceivingHeader processarNovaNotaFiscal(ReceivingHeader header, List<ReceivingLine> lines) {
        if (headerRepository.findByNumeroNotaFiscal(header.getNumeroNotaFiscal()).isPresent()) {
            throw new IllegalArgumentException("NF já registrada.");
        }
        header.setStatus(StatusRecebimento.AGUARDANDO_RECEBIMENTO);
        ReceivingHeader savedHeader = headerRepository.save(header);
        for (ReceivingLine line : lines) {
            line.setHeader(savedHeader);
            line.setStatus(StatusRecebimento.AGUARDANDO_RECEBIMENTO);
            lineRepository.save(line);
        }
        return savedHeader;
    }

    public Optional<ReceivingHeader> buscarPorNumeroNota(String numeroNF) {
        return headerRepository.findByNumeroNotaFiscal(numeroNF);
    }

    public ReceivingHeader buscarHeaderPorNF(String numeroNF) {
        return headerRepository.findByNumeroNotaFiscal(numeroNF)
                .orElseThrow(() -> new IllegalArgumentException("NF não encontrada."));
    }
}