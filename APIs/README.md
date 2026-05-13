# Lab APIs

**Autor:** Esteban Barrera Sanabria
**Fecha:** Abril 2025  

## Archivos del laboratorio

| Archivo | Descripción |
| --- | --- |
| `REST_The Lord of the Rings.postman_collection.json` | Colección REST de The One API, para el universo cinematogracido de The Lord Of The Rings |
| `Tarea GraphQL — Esteban Barrera Sanabria.postman_collection.json` | Colección GraphQL de Countries |

## Parte 1) REST API: The Lord of the Rings

**URL base:** `https://the-one-api.dev/v2`  

### Pregunta 1: ¿Qué API elegiste y por qué?

Elegí The Lord of the Rings API (The One API) por tres razones principales:

1. Desde una previsualización de los datos estos se ven bastante completos y relacionados entre sí, incluye detalles cinematograficos interesantes

2. Nace desde un gusto personal por la serie de peliculas y en general el universo de J.R.R Tolkien, dada la amplitud de las producciones en la fantasía como género y la variabilidad y naturaleza de personajes, lugares, razas, etc.

### Pregunta 2: ¿Qué datos devuelve?

La API devuelve datos en formato JSON con una estructura paginada consistente:

```json
{
  "docs": [...],
  "total": 8,
  "limit": 1000,
  "offset": 0,
  "page": 1,
  "pages": 1
}
```

Los campos `docs`, `total`, `limit`, `offset`, `page`, y `pages` aparecen en todas las respuestas como predeterminado.

Para `/movie`, cada película contiene:

```json
{
  "_id": "5cd95395807270f3d024ed",
  "name": "The Lord of the Rings Series",
  "runtimeInMinutes": 558,
  "budgetInMillions": 281,
  "boxOfficeRevenueInMillions": 2917,
  "academyAwardNominations": 30,
  "academyAwardWins": 17,
  "rottenTomatoesScore": 94
}
```

Para `/character`, cada personaje contiene:

```json
{
  "_id": "5cd99d4bde30eff6ebccfbbe",
  "height": "",
  "race": "Human",
  "gender": "Female",
  "birth": "",
  "spouse": "Khamûl",
  "death": "",
  "realm": "",
  "hair": "",
  "name": "Adanel",
  "wikiUrl": "http://lotr.wikia.com//wiki/Adanel"
}
```

Para cada `/quote`, cada frase contiene:

```json
{
  "_id": "5cd96e05de30eff6ebcce7e9",
  "dialog": "Deagol!",
  "movie": "5cd95395807270f3d024ed",
  "character": "5cd99d4bde30eff6ebccfc15",
  "id": "5cd96e05de30eff6ebcce7e9"
}
```

### Pregunta 3: ¿Usa token o no? ¿Qué tipo?

Sí, las consultas usan token, el tipo es Bearer Token, también llamado Token de Portador.

Despues de refistrarse en The One Api, la plataforma genera un token único y en cada request se envía dentro del header HTTP, de esta manera

```bash
Authorization: Bearer kMn8xPq2...
```

### Pregunta 4: ¿Qué código de estado recibiste en cada request?

| # | Request | Código | Significado |
| --- | --- | --- | --- |
| 1 | GET /movie (todas las películas) | **200 OK** | Éxito, con token válido |
| 2 | GET /movie/:id (una película) | **200 OK** | Película encontrada por ID |
| 3 | GET /character?limit=5&page=1 | **200 OK** | Paginación funcionó correctamente |
| 4 | GET /quote?limit=3&page=1 | **200 OK** | Frases obtenidas con filtro |
| 5 | GET /movie SIN token | **401 Unauthorized** | Token requerido y ausente |
| 6 | GET /movie/:id/quote (relación) | **200 OK** | Frases de una película específica |

Para cada request hay evidencias fotograficas tambien almacenadas en `/media` para mas interés, cada request tiene dos archivos, el primero mostrando el `Body` resultante y la segunda mostrando los `Tests` y a veces la consola, pero ambos de la misma llamada.

Body de todas las peliculas:

![uno](./media/lotr1.1.gif)

Tests para todas las peliculas:

![dos](./media/lotr1.2.png)

Body de una pelicula en especifico, en este caso la API guardo la trilogia de El Señor de Los Anillos como una sola, The Lord of The Rings Series:

![tres](./media/lotr2.1.png)

Tests para una pelicula en especifico:

![cuatro](./media/lotr2.2.png)

Body de los personajes, limitado a 5 ocurrencias:

![cinco](./media/lotr3.1.gif)

Tests para los personajes:

![seis](./media/lotr3.2.png)

Body de las frases aleatorias de una misma pelicula, en este caso se usa El Retorno del Rey:

![siete](./media/lotr4.1.png)

Tests para las frases aleatorias de una misma pelicula, El Retorno del Rey:

![ocho](./media/lotr4.2.png)

En este caso solo se muestra el Body porque los Tests pasaron y puede confundir al usuario siendo que la request fue inválida:

![nueve](./media/lotr5.png)

Body de todas las frases de una misma pelicula, se continua usando El Retorno del Rey:

![diez](./media/lotr6.1.gif)

Tests para todas las frases de una misma pelicula, El Retorno del Rey:

![once](./media/lotr6.2.png)

### Pregunta 5: ¿Qué aprendiste diferente a JSONPlaceholder?

JSONPlaceholder no tiene ningún tipo de autenticación, por lo que cualquier persona puede hacer cualquier request. Con LOTR API aprendí cómo funciona el flujo de Bearer: obtener token → enviarlo en el header → recibir datos protegidos.

Además, JSONPlaceholder devuelve arrays planos: `[{id:1, ...}, {id:2, ...}]`. LOTR API devuelve un objeto paginad con metadatos: `{docs: [...], total: 8, page: 1, pages: 1}`, en la practica se ve mas eficiente y profesional.

## Parte 2) GraphQL: Countries API

**Endpoint:** `https://countries.trevorblades.com/graphql`  

### Requests creados

| # | Query | Descripción |
| --- | --- | --- |
| 1 | `countries { code name capital emoji }` | Todos los países con campos básicos |
| 2 | `country(code: "CO") { ... continent { name } languages { name } }` | Colombia con continente e idiomas |
| 3 | `continents { name countries { code name capital emoji } }` | Todos los continentes con sus países (anidado) |
| 4 | `languages { code name native rtl }` | Todos los idiomas disponibles |
| 5 | `continent(code: "SA") { countries { name languages { name } } }` | Sudamérica con países e idiomas (3 niveles) |

Tambien se tiene evidencia visual de los resultados de la misma forma:

Body de todas los paises con campos basicos:

![uno](./media/graphql1.1.gif)

Tests para todas los paises con campos basicos:

![dos](./media/graphql1.2.png)

Body del pais especificado en el query (Colombia -> CO):

![tres](./media/graphql2.1.png)

Tests para el pais especificado en el query (Colombia -> CO):

![cuatro](./media/graphql2.2.png)

Body de los continentes con sus paises anidados:

![cinco](./media/graphql3.1.gif)

Tests para los continentes con sus paises anidados:

![seis](./media/graphql3.2.png)

Body de los lenguajes con campos básicos:

![siete](./media/graphql4.1.gif)

Tests para los lenguajes con campos básicos:

![ocho](./media/graphql4.2.png)

Body de continente con código (Sudamerica -> SA) con paises e idiomas anidados:

![nueve](./media/graphql5.1.gif)

Tests para continente con código (Sudamerica -> SA) con paises e idiomas anidados:

![diez](./media/graphql5.2.png)

### Pregunta 1: ¿Qué diferencia encontraste vs REST?

La diferencia clave es que mientras que en REST cada recurso tiene su propia URL: `/movie`, `/character`, `/quote`, en GraphQL existe un único endpoint (`/graphql`), ademas que en la query se envía el mismo body deseado, por lo tanto se recibe exactamente lo que se necesita si ncampos innecesarios, que muchas pasa en REST.

Otra caracteristica diferencial es que para obtener un país en su continente y sus idiomas necesité de una sola query y en REST, por su underfetching, deben ser 3 request separados, que es lo que se explica en la respuesta de la Pregunta 2.

Por último pude identificar que en REST la URL define que se pide según el método necesario: `GET /movie/123`, y en GraphQL siempre es `POST /graphql`.

### Pregunta 2: ¿Cuántos requests REST necesitarías para reemplazar tu query más compleja?

La query más compleja es la Request 5: Continente → Países → Idiomas.

```graphql
query ContinenteConPaisesEIdiomas($codigo: ID!) {
  continent(code: $codigo) {
    name
    countries {
      name
      capital
      emoji
      languages {
        code
        name
      }
    }
  }
}
```

Por tanto, en REST para obtener los mismos datos se debe:

1. `GET /continents/SA`: obtener datos del continente SA
2. `GET /countries?continent=SA`: obtener los países de Sudamérica (~12 países)
3. Para cada país: `GET /countries/CO/languages`, `GET /countries/BR/languages`... → 12 requests más

El total es de alrededor 14 requests contra una sola.

### Pregunta 3: ¿En qué proyecto real usarías GraphQL?

El contexto podria ser el de la universidad, aplicado a tramites, solicitudes, plataformas digitales y tantos contextos donde el estudiante haga llamadas constantes, por ejemplo, el estudiante necesita ver en su Sistema de Información Académica:

- Nombre de la asignatura
- Créditos
- Profesor
- Horario

La cantidad de estudiantes, materias, profesores y en general registros hace que sea mas facil realizar las consultas en menos requests que posiblemente pediria REST para el caso.
