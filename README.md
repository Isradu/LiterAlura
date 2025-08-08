<h1 align="center"> 📚 LiterAlura - Catálogo de Libros y Autores </h1>

<img width="846" height="222" alt="Captura de Pantalla 2025-08-07 a la(s) 8 08 54 p m" src="https://github.com/user-attachments/assets/6a9f8d7b-d33c-451e-8850-e0afdd664853" />

> *Imagen: Una captura de pantalla de la aplicación en funcionamiento, mostrando la persistencia de datos de la aplicación mediante los resultados de una búsqueda.*

## 🚀 Insignias

![GitHub language count](https://img.shields.io/github/languages/count/isradu/LiterAlura)
![GitHub top language](https://img.shields.io/github/languages/top/isradu/LiterAlura)
![GitHub repo size](https://img.shields.io/github/repo-size/isradu/LiterAlura)
![License](https://img.shields.io/github/license/isradu/LiterAlura)
<img src="https://img.shields.io/badge/STATUS-EN%20DESAROLLO-green">




## 📖 Índice
- [Descripción del Proyecto](#descripción-del-proyecto)
- [Estado del Proyecto](#estado-del-proyecto)
- [Demostración de Funciones](#demostración-de-funciones)
- [Acceso al Proyecto](#acceso-al-proyecto)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Personas Desarrolladoras del Proyecto](#personas-desarrolladoras-del-proyecto)
- [Licencia](#licencia)




## 📝 Descripción del Proyecto

LiterAlura es una aplicación de consola desarrollada en Java con el framework Spring Boot que permite a los usuarios interactuar con un catálogo de libros. La aplicación se conecta a la API de [Gutendex](https://gutendex.com/) para buscar libros y los gestiona en una base de datos PostgreSQL local, evitando duplicados y facilitando su consulta posterior.

## 🚧 Estado del Proyecto
✅ **En desarrollo.** Las funcionalidades principales están implementadas y operativas. El proyecto está listo para ser probado y mejorado.




## 💡 Demostración de Funciones

La aplicación ofrece un menú interactivo que permite realizar diversas acciones:

1.  **Buscar libros por título:**
    * Busca libros en la API y permite al usuario elegir de una lista para guardar el de su preferencia.
2.  **Listar libros y autores:**
    * Muestra los libros y autores que se han guardado en la base de datos.
3.  **Filtrado avanzado:**
    * Permite encontrar autores vivos en un año específico y libros por idioma.
4.  **Estadísticas y Top 10:**
    * Genera estadísticas detalladas sobre las descargas de los libros y muestra los 10 más descargados.




## ⚙️ Acceso al Proyecto

Para probar la aplicación en tu entorno local, sigue estos pasos:

### Requisitos Previos
Asegúrate de tener instalado:
- **Java JDK 17** o superior.
- **Maven**.
- **PostgreSQL** (versión 12 o superior).




### Configuración
1.  **Clona el repositorio:**
    ```bash
    git clone [https://github.com/tu-usuario/literAlura.git](https://github.com/tu-usuario/literAlura.git)
    cd literAlura
    ```
2.  **Configura la Base de Datos:**
    - Crea una base de datos en PostgreSQL con el nombre `literAlura`.
    - Copia el archivo `src/main/resources/application.properties.example` y renómbralo a `application.properties`.
    - Abre el archivo `application.properties` y configura tus credenciales de PostgreSQL.
3.  **Ejecuta la Aplicación:**
    - Abre el proyecto en tu IDE (IntelliJ IDEA, por ejemplo).
    - Ejecuta la clase principal `LiterAluraApplication.java`.
    - Alternativamente, puedes usar Maven desde la terminal:
        ```bash
        ./mvnw spring-boot:run
        ```



## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Maven**
- **PostgreSQL**
- **API Gutendex**



## 👥 Personas Desarrolladoras del Proyecto
- **[Israel Durán](https://github.com/Isradu)**

## 📜 Licencia
Este proyecto está bajo la Licencia MIT. Para más detalles, consulta el archivo [LICENSE.md](LICENSE.md).
