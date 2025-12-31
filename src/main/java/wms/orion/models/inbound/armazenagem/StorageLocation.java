package wms.orion.models.inbound.armazenagem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "wms_storage_location")
@Getter
@Setter
public class StorageLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String locationCode; // Ex: R01-P02-N03

    private String zone; // Ex: PALLET, PICKING
    private boolean occupied = false;
}