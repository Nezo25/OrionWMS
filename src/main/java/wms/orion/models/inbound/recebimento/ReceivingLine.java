    package wms.orion.models.inbound.recebimento;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import lombok.Getter;
    import lombok.Setter;
    import wms.orion.models.client.User;
    import wms.orion.models.enums.StatusRecebimento;
    import jakarta.persistence.*;

    @Entity
    @Table(name = "inb_receiving_line")
    @Getter
    @Setter
    public class ReceivingLine {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
        @ManyToOne
        @JsonBackReference
        @JoinColumn(name = "header_id", nullable = false)
        private ReceivingHeader header;
        private String sku;
        private int quantidadeEsperada;
        private int quantidadeRecebida = 0;
        private String lote;
        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        private StatusRecebimento status;
        @Column(name = "license-plate")
        private String licensePlate;
    }