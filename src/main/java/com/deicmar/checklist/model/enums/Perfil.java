package com.deicmar.checklist.model.enums;

public enum Perfil {
    OPERADOR("Operador"),
    SUPERVISOR("Supervisor"),
    ADMIN("Administrador");

    private final String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
