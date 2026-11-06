package com.eventostec.api.domain.image;

public record ImageFile(String originalName, String contentType, byte[] content) {}
