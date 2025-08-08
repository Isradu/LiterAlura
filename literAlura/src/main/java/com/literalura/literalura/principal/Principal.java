package com.literalura.literalura.principal;

import com.literalura.literalura.entity.Autor;
import com.literalura.literalura.entity.Libro;
import com.literalura.literalura.model.DatosLibro;
import com.literalura.literalura.model.DatosAutor;
import com.literalura.literalura.model.DatosRespuestaApi;
import com.literalura.literalura.repository.AutorRepository;
import com.literalura.literalura.repository.LibroRepository;
import com.literalura.literalura.service.ConsumoAPI;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoAPI;
    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;

    public Principal(ConsumoAPI consumoAPI, AutorRepository autorRepository, LibroRepository libroRepository) {
        this.consumoAPI = consumoAPI;
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    -----------------------------------------
                    Seleccione una opción:
                    1 - Buscar libros por título (desde la API)
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Buscar libros por autor (desde la API)
                    7 - Buscar libros por tema (desde la API)
                    8 - Generar estadísticas y Top 10
                    0 - Salir
                    -----------------------------------------
                    """;
            System.out.println(menu);

            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibros();
                        break;
                    case 3:
                        listarAutores();
                        break;
                    case 4:
                        listarAutoresVivos();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        buscarLibroPorAutor();
                        break;
                    case 7:
                        buscarLibroPorTema();
                        break;
                    case 8:
                        generarEstadisticasYTop10();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        System.exit(0); // para terminar la aplicación completamente
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Opción no válida: Por favor, ingrese un número del 0 al 7.");
                teclado.nextLine(); // Consumir la entrada inválida para evitar un bucle infinito
                opcion = -1; // para que el bucle siga pidiendo una opción
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        var tituloLibro = teclado.nextLine();
        DatosRespuestaApi datosBusqueda = consumoAPI.obtenerDatosBusqueda(tituloLibro);

        if (datosBusqueda != null && datosBusqueda.resultados() != null && !datosBusqueda.resultados().isEmpty()) {
            List<DatosLibro> resultados = datosBusqueda.resultados();

            // Mostramos los resultados al usuario
            System.out.println("\n--- Resultados de la búsqueda ---");
            for (int i = 0; i < resultados.size(); i++) {
                DatosLibro datosLibro = resultados.get(i);
                String autorNombre = datosLibro.autores().isEmpty() ? "Desconocido" : datosLibro.autores().get(0).nombre();
                System.out.println((i + 1) + ". Título: " + datosLibro.titulo() + " | Autor: " + autorNombre);
            }

            // Pedimos al usuario que elija un libro para guardar
            System.out.println("\nEscribe el número del libro que deseas guardar, o '0' para volver al menú principal:");
            int seleccion = teclado.nextInt();
            teclado.nextLine();
            if (seleccion > 0 && seleccion <= resultados.size()) {
                DatosLibro datosLibroSeleccionado = resultados.get(seleccion - 1);

                // Lógica de persistencia para el libro seleccionado
                // Verificamos si el libro ya existe en nuestra base de datos por el título
                Optional<Libro> libroExistente = libroRepository.findByTitulo(datosLibroSeleccionado.titulo());
                if (libroExistente.isPresent()) {
                    System.out.println("\n--- El ibro ya está registrado ---");
                    System.out.println(libroExistente.get());
                } else {
                    // Si el libro no existe lo creamos y guardamos
                    Autor autor = null;
                    if (datosLibroSeleccionado.autores() != null && !datosLibroSeleccionado.autores().isEmpty()) {
                        DatosAutor datosAutor = datosLibroSeleccionado.autores().get(0);
                        // Verificamos si el autor ya existe en la base de datos
                        Optional<Autor> autorExistente = autorRepository.findByNombre(datosAutor.nombre());
                        if (autorExistente.isPresent()) {
                            autor = autorExistente.get();
                        } else {
                            // Si el autor no existe, lo creamos y guardamos
                            autor = new Autor();
                            autor.setNombre(datosAutor.nombre());
                            autor.setFechaNacimiento(datosAutor.fechaNacimiento());
                            autor.setFechaFallecimiento(datosAutor.fechaFallecimiento());
                            autorRepository.save(autor);
                        }
                    }
                    // Creación y guardado del libro
                    Libro nuevoLibro = new Libro();
                    nuevoLibro.setTitulo(datosLibroSeleccionado.titulo());
                    nuevoLibro.setIdioma(datosLibroSeleccionado.idiomas().isEmpty() ? null : datosLibroSeleccionado.idiomas().get(0));
                    nuevoLibro.setNumeroDescargas(datosLibroSeleccionado.numeroDescargas());
                    nuevoLibro.setAutor(autor);
                    libroRepository.save(nuevoLibro);
                    System.out.println("\n--- Libro registrado exitosamente ---");
                    System.out.println(nuevoLibro);
                }
            } else if (seleccion == 0) {
                System.out.println("Operación cancelada.");
            } else {
                System.out.println("Selección inválida. Intente de nuevo.");
            }
        } else {
            System.out.println("\nNo se encontraron libros con ese título.");
        }
    }


    private void listarLibros() {
        System.out.println("\n--- Libros registrados ---");
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void listarAutores() {
        System.out.println("\n--- Autores registrados ---");
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivos() {
        while (true) {
            System.out.println("Escribe el año para verificar los autores vivos en ese año:");
            try {
                var anio = teclado.nextInt();
                teclado.nextLine();
                List<Autor> autoresVivos = autorRepository.findByFechaNacimientoLessThanEqualAndFechaFallecimientoGreaterThanEqual(anio, anio);
                if (autoresVivos.isEmpty()) {
                    System.out.println("\nNo se encontraron autores vivos en ese año.");
                } else {
                    System.out.println("\n--- Autores vivos en el año " + anio + " ---");
                    autoresVivos.forEach(System.out::println);
                }
                break; // Salir del bucle si la entrada es válida
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Por favor, ingrese un año válido (número entero).");
                teclado.nextLine(); // Consumir la entrada inválida para evitar un bucle infinito
                System.out.println("¿Deseas volver a intentar? (s/n)");
                String opcion = teclado.nextLine();
                if (!opcion.equalsIgnoreCase("s")) {
                    break; // Salir del bucle si el usuario no quiere reintentar
                }
            }
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Escribe el idioma para buscar:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        var idioma = teclado.nextLine();

        // Buscar libros por idioma usando un método del repositorio
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("\nNo se encontraron libros en ese idioma.");
        } else {
            System.out.println("\n--- Libros en idioma " + idioma + " ---");
            librosPorIdioma.forEach(System.out::println);
        }
    }

    private void buscarLibroPorAutor() {
        System.out.println("Escribe el nombre del autor para buscar libros (Ejemplo: \"Edgar Allan Poe\"):");
        var nombreAutor = teclado.nextLine();

        // 1. Búsqueda amplia en la API usando el parámetro 'search'
        DatosRespuestaApi datosBusqueda = consumoAPI.obtenerDatosBusqueda(nombreAutor);
        if (datosBusqueda != null && datosBusqueda.resultados() != null && !datosBusqueda.resultados().isEmpty()) {
            // 2. Filtrado estricto en la aplicación usando Streams de Java
            List<DatosLibro> resultadosFiltrados = datosBusqueda.resultados().stream()
                    .filter(l -> l.autores().stream()
                            .anyMatch(a -> a.nombre().toLowerCase().contains(nombreAutor.toLowerCase())))
                    .collect(Collectors.toList());

            if (!resultadosFiltrados.isEmpty()) {
                // Muestrar los resultados filtrados y pedir al usuario que elija
                procesarResultadosBusqueda(new DatosRespuestaApi(resultadosFiltrados));
            } else {
                System.out.println("\nNo se encontraron libros para ese autor.");
            }
        } else {
            System.out.println("\nNo se encontraron libros para ese autor en la API.");
        }
    }

    private void buscarLibroPorTema() {
        System.out.println("""
            Escribe el tema para buscar libros. Algunos ejemplos:
            - drama
            - fiction
            - romance
            - poetry
            - science
            - history
            """);
        var tema = teclado.nextLine();
        DatosRespuestaApi datosBusqueda = consumoAPI.obtenerDatosPorTema(tema);
        procesarResultadosBusqueda(datosBusqueda);
    }

    // Método para procesar y guardar los resultados de la búsqueda de forma genérica
    private void procesarResultadosBusqueda(DatosRespuestaApi datosBusqueda) {
        if (datosBusqueda != null && datosBusqueda.resultados() != null && !datosBusqueda.resultados().isEmpty()) {
            List<DatosLibro> resultados = datosBusqueda.resultados();

            System.out.println("\n--- Resultados de la búsqueda ---");
            for (int i = 0; i < resultados.size(); i++) {
                DatosLibro datosLibro = resultados.get(i);
                String autorNombre = datosLibro.autores().isEmpty() ? "Desconocido" : datosLibro.autores().get(0).nombre();
                System.out.println((i + 1) + ". Título: " + datosLibro.titulo() + " | Autor: " + autorNombre);
            }

            System.out.println("\nEscribe el número del libro que deseas guardar, o '0' para cancelar:");
            int seleccion = teclado.nextInt();
            teclado.nextLine();

            if (seleccion > 0 && seleccion <= resultados.size()) {
                DatosLibro datosLibroSeleccionado = resultados.get(seleccion - 1);
                Optional<Libro> libroExistente = libroRepository.findByTitulo(datosLibroSeleccionado.titulo());
                if (libroExistente.isPresent()) {
                    System.out.println("\n--- Libro ya registrado ---");
                    System.out.println(libroExistente.get());
                } else {
                    Autor autor = null;
                    if (datosLibroSeleccionado.autores() != null && !datosLibroSeleccionado.autores().isEmpty()) {
                        DatosAutor datosAutor = datosLibroSeleccionado.autores().get(0);
                        Optional<Autor> autorExistente = autorRepository.findByNombre(datosAutor.nombre());
                        if (autorExistente.isPresent()) {
                            autor = autorExistente.get();
                        } else {
                            autor = new Autor();
                            autor.setNombre(datosAutor.nombre());
                            autor.setFechaNacimiento(datosAutor.fechaNacimiento());
                            autor.setFechaFallecimiento(datosAutor.fechaFallecimiento());
                            autorRepository.save(autor);
                        }
                    }

                    Libro nuevoLibro = new Libro();
                    nuevoLibro.setTitulo(datosLibroSeleccionado.titulo());
                    nuevoLibro.setIdioma(datosLibroSeleccionado.idiomas().isEmpty() ? null : datosLibroSeleccionado.idiomas().get(0));
                    nuevoLibro.setNumeroDescargas(datosLibroSeleccionado.numeroDescargas());
                    nuevoLibro.setAutor(autor);
                    libroRepository.save(nuevoLibro);
                    System.out.println("\n--- Libro registrado exitosamente ---");
                    System.out.println(nuevoLibro);
                }
            } else if (seleccion != 0) {
                System.out.println("Selección inválida. Intente de nuevo.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } else {
            System.out.println("\nNo se encontraron libros en la API para esa búsqueda.");
        }
    }

    private void generarEstadisticasYTop10() {
        // Obtenemos todos los libros de nuestra base de datos
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados para generar estadísticas.");
            return;
        }
        System.out.println("\n--- Estadísticas de descargas ---");
        // Usamos DoubleSummaryStatistics para obtener datos estadísticos
        DoubleSummaryStatistics stats = libros.stream()
                .mapToDouble(Libro::getNumeroDescargas)
                .summaryStatistics();

        System.out.printf("Número de libros: %d%n", stats.getCount());
        System.out.printf("Número total de descargas: %.0f%n", stats.getSum());
        System.out.printf("Promedio de descargas: %.2f%n", stats.getAverage());
        // Búsqueda del libro con el máximo de descargas
        Optional<Libro> libroConMaxDescargas = libros.stream()
                .max(Comparator.comparing(Libro::getNumeroDescargas));
        libroConMaxDescargas.ifPresent(libro ->
                System.out.printf("Libro con más descargas: %.0f (Título: %s)%n",
                        stats.getMax(), libro.getTitulo()));
        // Búsqueda del libro con el mínimo de descargas
        Optional<Libro> libroConMinDescargas = libros.stream()
                .min(Comparator.comparing(Libro::getNumeroDescargas));
        libroConMinDescargas.ifPresent(libro ->
                System.out.printf("Libro con menos descargas: %.0f (Título: %s)%n",
                        stats.getMin(), libro.getTitulo()));
        // Mostrar el Top 10
        System.out.println("\n--- Top 10 libros más descargados ---");
        List<Libro> top10Libros = libroRepository.findTop10ByOrderByNumeroDescargasDesc();
        top10Libros.forEach(libro ->
                System.out.printf("Título: %s | Descargas: %d%n",
                        libro.getTitulo(), libro.getNumeroDescargas()));
    }
}
