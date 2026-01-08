package wms.orion.dto.inboundDTO.armazenagem;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutawayRequestDTO {

    @NotBlank(message = "O License Plate (LP) é obrigatório.")
    private String licensePlate;

    @NotBlank(message = "O endereço de destino é obrigatório.")
    private String locationCode;
}