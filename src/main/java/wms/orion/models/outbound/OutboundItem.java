package wms.orion.models.outbound;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wms.orion.models.client.User;

@Entity
@Getter
@Setter
public class OutboundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OutboundOrder order;

    private String sku;
    private Integer quantityRequested; // Qtd solicitada pelo cliente
    private Integer quantityPicked = 0; // Qtd já bipada pelo operador

}
