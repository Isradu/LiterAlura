package com.literalura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // ignorar propiedades desconocidas del JSON que no están en esta clase
public record DatosRespuestaApi(
        @JsonAlias("results") List<DatosLibro> resultados
) {
}
