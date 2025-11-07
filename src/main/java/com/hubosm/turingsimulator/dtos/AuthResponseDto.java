package com.hubosm.turingsimulator.dtos;

public record AuthResponseDto(String accessToken, String tokenType, long expiresInSeconds){ }
