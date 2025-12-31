package wms.orion.models.enums;

public enum StatusRecebimento {


    AGUARDANDO_RECEBIMENTO, // NF importada, mas conferência não começou
    EM_CONFERENCIA,         // Conferência em progresso
    RECEBIMENTO_CONCLUIDO,  // Documento finalizado e validado

    // Status do Estoque (WarehouseInventory) - Transição para o próximo módulo
    ESTOQUE_AREA_RECEBIMENTO, // Item existe, está na doca, pronto para endereçamento
    AGUARDANDO_ARMAZENAGEM, ESTOQUE_DISPONIVEL, RECEBIDO, ESTOQUE_ENDERECO_FINAL
}