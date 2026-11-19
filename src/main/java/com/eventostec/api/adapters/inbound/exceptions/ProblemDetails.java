package com.eventostec.api.adapters.inbound.exceptions;

/**
 * @param title    resumo curto e legível do tipo de problema (ex.: "Não encontrado")
 * @param status   código de status HTTP numérico (ex.: 404), conforme a RFC7807
 * @param error    nome textual do status HTTP (ex.: "NOT_FOUND")
 * @param detail   explicação específica do problema ocorrido, voltada ao cliente da API
 * @param instance URI da requisição que originou o erro
 */
public record ProblemDetails(String title, Integer status, String error, String detail, String instance) {
}
