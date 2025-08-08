package com.literalura.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component; // Importante para que Spring la gestione

// Clase para Convertir datos de una API a un formato para usar en nuestro proyecto.
@Component // Para indicar que esta clase es un componente de Spring
public class ConvierteDatos implements IConvierteDatos {
    // Objeto que mapea datos obtenidos de una API a un formato que se pueda usar en nuestro proyecto.
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override // Sobreescribe el metodo de la interfaz IConvierteDatos.
    public <T> T obtieneDatos(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
