package com.literalura.literalura.service;

import com.literalura.literalura.model.DatosRespuestaApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ConsumoAPI {

    private final RestClient restClient;
    private final IConvierteDatos conversor;

    // Constructor que inyecta RestClient.Builder y el conversor de datos
    public ConsumoAPI(RestClient.Builder restClientBuilder, IConvierteDatos conversor) {
        this.restClient = restClientBuilder.build();
        this.conversor = conversor; // Inyección de la interfaz IConvierteDatos
    }

    // Método que ahora devuelve un objeto DatosRespuestaApi
    public DatosRespuestaApi obtenerDatosBusqueda(String searchTerm) {
        String json = restClient.get()
                .uri("/books/?search={term}", searchTerm)
                .retrieve()
                .body(String.class);
        // Usamos el conversor para mapear el JSON a un objeto DatosRespuestaApi
        return conversor.obtieneDatos(json, DatosRespuestaApi.class);
    }

    // Método para buscar libros por idioma, también devuelve un objeto
    public DatosRespuestaApi obtenerDatosPorIdioma(String languageCode) {
        String json = restClient.get()
                .uri("/books/?languages={lang}", languageCode)
                .retrieve()
                .body(String.class);
        return conversor.obtieneDatos(json, DatosRespuestaApi.class);
    }

    // Método para buscar libros por autor
    public DatosRespuestaApi obtenerDatosPorAutor(String autor) {
        String json = restClient.get()
                .uri("/books/?author={autor}", autor)
                .retrieve()
                .body(String.class);
        return conversor.obtieneDatos(json, DatosRespuestaApi.class);
    }
    // Método para buscar libros por temática
    public DatosRespuestaApi obtenerDatosPorTema(String tema) {
        String json = restClient.get()
                .uri("/books/?topic={tema}", tema)
                .retrieve()
                .body(String.class);
        return conversor.obtieneDatos(json, DatosRespuestaApi.class);
    }
}
