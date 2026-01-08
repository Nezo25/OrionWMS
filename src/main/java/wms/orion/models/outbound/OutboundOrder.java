package wms.orion.models.outbound;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wms.orion.models.enums.OutboundStatus;

import java.util.List;

@Getter
@Setter
@Entity
public class OutboundOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OutboundStatus status;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    private List<OutboundItem> items;

}
