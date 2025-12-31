package wms.orion.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.orion.dto.armazenagem.PutawayRequestDTO;
import wms.orion.dto.armazenagem.StorageResponseDTO;
import wms.orion.models.inventory.WarehouseInventory;
import wms.orion.services.armazenagem.StorageService;

@RestController
@RequestMapping("/api/wms/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * POST /putaway
     * Recebe um JSON com licensePlate e locationCode.
     * Valida se o pallet foi recebido antes de mover para o estoque.
     */
    @PostMapping("/putaway")
    public ResponseEntity<?> armazenarPallet(@RequestBody @Valid PutawayRequestDTO request) {
        try {
            WarehouseInventory pallet = storageService.realizarArmazenagem(
                    request.getLicensePlate(),
                    request.getLocationCode()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(new StorageResponseDTO(pallet));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /pallet/{lp}
     * Consulta detalhes do pallet para conferência rápida.
     */
    @GetMapping("/pallet/{lp}")
    public ResponseEntity<?> buscarPallet(@PathVariable String lp) {
        try {
            WarehouseInventory pallet = storageService.buscarPalletPorLp(lp);
            return ResponseEntity.ok(new StorageResponseDTO(pallet));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pallet não localizado.");
        }
    }

    /**
     * DELETE /pallet/{lp}
     * Deleta o pallet se ele não estiver em processo de expedição.
     */
    @DeleteMapping("/pallet/{lp}")
    public ResponseEntity<?> excluirPallet(@PathVariable String lp) {
        try {
            storageService.excluirPallet(lp);
            return ResponseEntity.ok("Pallet " + lp + " removido com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}