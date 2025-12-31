package wms.orion.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.orion.dto.ReceivingHeaderDTO;
import wms.orion.dto.recebimento.ConferenciaDTO;
import wms.orion.models.inbound.recebimento.ReceivingHeader;
import wms.orion.models.inbound.recebimento.ReceivingLine;
import wms.orion.models.inventory.WarehouseInventory;
import wms.orion.models.repositories.InventoryRepository; // Certifique-se deste import
import wms.orion.services.recebimento.ReceivingService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wms/inbound")
public class ReceivingController {

    private final ReceivingService receivingService;
    private final InventoryRepository inventoryRepository;

    public ReceivingController(ReceivingService receivingService, InventoryRepository inventoryRepository) {
        this.receivingService = receivingService;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Registra a Nota Fiscal e suas linhas esperadas no sistema.
     */
    @PostMapping("/nf")
    public ResponseEntity<?> processarNovaNotaFiscal(@RequestBody ReceivingHeaderDTO dto) {
        try {
            ReceivingHeader header = new ReceivingHeader();
            header.setNumeroNotaFiscal(dto.numeroNotaFiscal());
            header.setDataChegada(dto.dataChegada());

            List<ReceivingLine> lines = dto.lines().stream()
                    .map(lineDto -> {
                        ReceivingLine line = new ReceivingLine();
                        line.setSku(lineDto.sku());
                        line.setQuantidadeEsperada(lineDto.quantidadeEsperada());
                        line.setLote(lineDto.lote());
                        return line;
                    })
                    .collect(Collectors.toList());

            ReceivingHeader savedHeader = receivingService.processarNovaNotaFiscal(header, lines);
            return ResponseEntity.status(HttpStatus.CREATED).body("NF Processada. ID: " + savedHeader.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Registra a conferência de um SKU e retorna o License Plate (LP) gerado para o pallet.
     */
    @PostMapping("/conferencia")
    public ResponseEntity<?> registrarConferencia(@RequestBody ConferenciaDTO dto) {
        try {
            String licensePlateGerado = receivingService.registrarConferencia(
                    dto.numeroNF(),
                    dto.sku(),
                    dto.quantidade(),
                    dto.lote()
            );

            return ResponseEntity.ok(Map.of(
                    "mensagem", "Leitura registrada com sucesso!",
                    "licensePlate", licensePlateGerado,
                    "localizacaoAtual", "DOCK-01"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/conferencias-detalhadas/{numeroNF}")
    public ResponseEntity<?> listarPalletsDaNF(@PathVariable String numeroNF) {
        try {
            // Busca o cabeçalho para obter o ID interno
            ReceivingHeader header = receivingService.buscarHeaderPorNF(numeroNF);
            List<WarehouseInventory> pallets = inventoryRepository.findByReceivingHeaderId(header.getId());

            return ResponseEntity.ok(pallets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao buscar conferências: " + e.getMessage());
        }
    }

    @GetMapping("/nf/{numeroNF}")
    public ResponseEntity<?> buscarNotaFiscal(@PathVariable String numeroNF) {
        Optional<ReceivingHeader> nf = receivingService.buscarPorNumeroNota(numeroNF);
        return nf.isPresent() ? ResponseEntity.ok(nf.get()) : ResponseEntity.status(404).body("Não encontrada.");
    }
}