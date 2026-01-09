package wms.orion.models.inbound.recebimento;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wms.orion.models.client.User;
import wms.orion.models.enums.StatusRecebimento;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name= "inb_receiving_header")
@Getter
@Setter
public class ReceivingHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // Chave de acesso da NF (garante que é única)
    @Column(name = "numero_nota_fiscal", unique = true, nullable = false)
    private String numeroNotaFiscal;
    private LocalDateTime dataChegada;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusRecebimento status;
    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReceivingLine> receivingLines;}