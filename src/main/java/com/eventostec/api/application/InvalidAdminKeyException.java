package com.eventostec.api.application;

public class InvalidAdminKeyException extends RuntimeException {
    public InvalidAdminKeyException() { super("Chave de administrador inválida"); }
}
