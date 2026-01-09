package wms.orion.models.inbound.armazenagem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wms.orion.models.client.User;

@Entity
@Table(name = "wms_storage_location")
@Getter
@Setter
public class StorageLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true, nullable = false)
    private String locationCode; // Ex: R01-P02-N03

    private String zone; // Ex: PALLET, PICKING
    private boolean occupied = false;
}