package com.literalura.literalura.service;

public interface IConvierteDatos {
    // Metodo de tipo generico que recibe un JSON y una clase y devuelve un objeto de clase genérica.
    <T> T obtieneDatos(String json, Class<T> clase);
}
