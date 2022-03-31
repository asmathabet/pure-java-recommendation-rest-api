## PURE-JAVA-RECOMMENDATION-REST-API

Ceci est un démo d'application développée en Java 8 en utilisant le module
[`jdk.httpserver`](https://docs.oracle.com/javase/10/docs/api/com/sun/net/httpserver/package-summary.html)  
ainsi que quelque libraries Java (comme [vavr](http://www.vavr.io/), [lombok](https://projectlombok.org/)).

## A propos du projet
Ce projet consiste à mettre enplace un mini système de recommandations de vidéo developpée en java pure.

## La classe Main 
C'est la première classe qui sera lancée. c'est elle qui gère les endpoints mis en place.

```java
import static com.consulner.app.Configuration.getErrorHandler;
import static com.consulner.app.Configuration.getObjectMapper;
import static com.consulner.app.Configuration.getVideoService;

import com.consulner.app.api.video.creation.CreationHandler;
import com.consulner.app.api.video.delete.DeleteHandler;
import com.consulner.app.api.video.search.SearchHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

class Application {

    
    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server;
        server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        CreationHandler creationHandler = new CreationHandler(getVideoService(), getObjectMapper(),
                getErrorHandler());
        SearchHandler searchHandler = new SearchHandler(getVideoService(), getObjectMapper(),
                getErrorHandler());
        DeleteHandler deleteHandler = new DeleteHandler(getVideoService(), getObjectMapper(),
                getErrorHandler());
        server.createContext("/api/videos/add", creationHandler::handle);
        server.createContext("/api/videos/get", searchHandler::handle);
        server.createContext("/api/videos/find-title", searchHandler::handle);
        server.createContext("/api/videos/find-similar", searchHandler::handle);
        server.createContext("/api/videos/get-films", searchHandler::handle);
        server.createContext("/api/videos/get-series", searchHandler::handle);
        server.createContext("/api/videos/get-deleted", searchHandler::handle);
        server.createContext("/api/videos/delete", deleteHandler::handle);

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
```
Lorsque vous exécutez le programme principal, il démarre le serveur Web sur le port `8000` et expose les différents endpoints.

par exemple. en utilisant curl:

```bash
curl localhost:8000/api/hello
```

Try it out yourself from branch:

```bash
git checkout step-2
```

## Premier endpoint

Le tout premier endpoint mis en place est `/api/videos/add`. Il est capable d’ajouter une vidéo. Une vidéo à 3 champs un id, un titre et un champ labels. Voici à quoi il ressemble: 

par exemple. en utilisant curl:
```bash
curl -X POST -H "Content-Type: application/json" \
    -d '{"id": "97e343ac-3141-45d1-aff6-68a7465d55ec","title": "matrix","labels": ["sci-fi","dystopia"]}' \
    localhost:8000/api/videos/add
```
vous allez avoir comme réponse l'id de video ajoutée:
```bash
{"id":"97e343ac-3141-45d1-aff6-68a7465d55ec"}
```

De même, on peut distinguer les types films, et séries.
Les règles pour identifier les types sont les suivantes :
1. Les films ont en plus un champ réalisateur et une date de sortie
2. Les séries ont un champ nombre d’épisodes
3. On veut que les champs soient exclusifs à ces types

par exemple, on peut ajouter une série en utilisant curl:
```bash
curl -X POST -H "Content-Type: application/json" \
    -d '{"id": "4544e617-84ab-4934-9bda-4d14c7ebcc19", "title": "Breaking Bad","number_of_episodes" : 62, "labels": ["chemistry","drug","desert","cancer"]}' \
    localhost:8000/api/videos/add
```
vous allez avoir comme réponse l'id de la série ajoutée:
```bash
{"id":"4544e617-84ab-4934-9bda-4d14c7ebcc19"}
```

on peut de même ajouter un film en utilisant curl:
```bash
curl -X POST -H "Content-Type: application/json" \
    -d '{"id": "86be99d4-ba36-11eb-8529-0242ac130003", "title": "Indiana Jones : Raiders of the Lost Ark", "director" : "Steven Spielberg", "release_date": "1982-03-18T12:00:00Z","labels": ["adventure","whip","archeology"]}' \
    localhost:8000/api/videos/add
```
vous allez avoir comme réponse l'id du film ajouté:
```bash
{"id":"86be99d4-ba36-11eb-8529-0242ac130003"}
```

## Deuxième endpoint

Le deuxième endpoint mis en place est `/api/videos/get`. Il est capable de retourner une vidéo via son id. Voici à quoi il ressemble: 

par exemple. en utilisant curl:
```bash
curl localhost:8000/api/videos/get?id=97e343ac-3141-45d1-aff6-68a7465d55ec
```

vous allez avoir comme réponse la video correspondante à l'id passé en paramètre:
```bash
{"video":{"id":"97e343ac-3141-45d1-aff6-68a7465d55ec","title":"matrix","labels":["sci-fi","dystopia"]}}
```

## Troisième endpoint

Le troisième endpoint mis en place est `/api/videos/find-title`. Il est capable de retourner une vidéo via son id. Voici à quoi il ressemble: 

par exemple. en utilisant curl:
```bash
curl localhost:8000/api/videos/find-title?title=matrix
```

vous allez avoir comme réponse la liste des videos ayant comme titre à celui passé en paramètre:
```bash
[{"video":{"id":"97e343ac-3141-45d1-aff6-68a7465d55ec","title":"matrix","labels":["sci-fi","dystopia"]}}]
```

## Quatrième endpoint

Le Quatrième endpoint mis en place est `/api/videos/delete`. Il est capable de supprimer une vidéos. Voici à quoi il ressemble: 

par exemple. en utilisant curl:
```bash
curl localhost:8000/api/videos/delete?id=97e343ac-3141-45d1-aff6-68a7465d55ec
```

## Cinquième endpoint

Le Cinquième endpoint mis en place est `api/videos/get-deleted`. Il est capable de retourner la liste des vidéos supprimés Voici à quoi il ressemble: 

par exemple. en utilisant curl:
```bash
curl localhost:8000/api/videos/get-deleted
```

vous allez avoir comme réponse la liste de tous les videos supprimés:
```bash
[{"video":{"id":"97e343ac-3141-45d1-aff6-68a7465d55ec","title":"matrix","labels":["sci-fi","dystopia"]}, "video":{"id": "86be99d4-ba36-11eb-8529-0242ac130003", "title": "Indiana Jones : Raiders of the Lost Ark", "director" : "Steven Spielberg", "release_date": "1982-03-18T12:00:00Z","labels": ["adventure","whip","archeology"]}}]
```

## Sixième endpoint

Le cinquième endpoint mis en place est `/api/videos/get-films`. Il est capable de retourner la liste des films. Voici à quoi il ressemble: 

par exemple. en utilisant curl:
```bash
curl localhost:8000/api/videos/get-films
```

vous allez avoir comme réponse la liste des film sauvegardés:
```bash
[{"video":{"id": "86be99d4-ba36-11eb-8529-0242ac130003", "title": "Indiana Jones : Raiders of the Lost Ark", "director" : "Steven Spielberg", "release_date": "1982-03-18T12:00:00Z","labels": ["adventure","whip","archeology"]}}]
```

## Septième endpoint

Le cinquième endpoint mis en place est `/api/videos/get-series`. Il est capable de retourner la liste des films. Voici à quoi il ressemble: 

par exemple. en utilisant curl:
```bash
curl localhost:8000/api/videos/get-series
```

vous allez avoir comme réponse la liste des film sauvegardés:
```bash
[{"video":{"id": "4544e617-84ab-4934-9bda-4d14c7ebcc19", "title": "Breaking Bad","number_of_episodes" : 62, "labels": ["chemistry","drug","desert","cancer"]}}]
```

## Huitième endpoint

Le quatrième endpoint mis en place est `/api/videos/find-similar`. Il permet à partir d’une vidéo et ses labels d'identifier une ou plusieurs autres vidéos qui sont similaires à cette vidéo. Le endpoint prend en paramètre un nombre qui correspond au nombre minimum de labels
que l’on veut en commun.Voici à quoi il ressemble: 

par exemple. en utilisant curl:
```bash
    curl -sS 'localhost:8000/api/videos/find-similar?id=97e343ac-3141-45d1-aff6-68a7465d55ec&deg=2'
```

vous allez avoir comme réponse la liste des videos qui sont similaires à cette vidéo:
```bash
[{"video":{"id":"86be99d4-ba36-11eb-8529-0242ac130003","title":"Indiana Jones : Raiders of the Lost Ark","labels":["adventure","whip","archeology"],"director":"Steven Spielberg","releaseDate":"1982-03-18T12:00:00Z"}},{"video":{"id":"4544e617-84ab-4934-9bda-4d14c7ebcc19","title":"Breaking Bad","labels":["chemistry","drug","desert","cancer"],"numberOfEpisodes":62}}]
```


## L'analyse des paramètres des requêtes
L'analyse des paramètres de requête est une autre fonctionnalité implémentée sans l'utilisation d'un framework.

L'analyse des paramètres se fait avec cette méthode

```java
public static Map<String, List<String>> splitQuery(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }

        return Pattern.compile("&").splitAsStream(query)
            .map(s -> Arrays.copyOf(s.split("="), 2))
            .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));

    }
```

elle est utilisée comme suit: 

```java
 Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
           
```

De même pour faire l'analyse du chemin:

```java
String path = exchange.getRequestURI().getPath();
```


## JSON, exception handlers and others

L'objet Video est défini comme suit: 

```java

@Data
@Builder
@AllArgsConstructor
public class Video {

    String id;
    String title;
    ArrayList<String> labels;
}
```

ainsi que les classes Film et Series:

```java
@Data
public class Film extends Video {
    String director;
    String releaseDate;

    /**
     *
     * @param director    director
     * @param releaseDate releaseDate
     */
    public Film(String id, String title, ArrayList<String> labels, String director, String releaseDate) {
        super(id, title, labels);
        this.director = director;
        this.releaseDate = releaseDate;
    }

}

@Data
public class Series extends Video {
    Integer numberOfEpisodes;

    /**
     *
     * @param numberOfEpisodes numberOfEpisodes
     */
    public Series(String id, String title, ArrayList<String> labels, Integer numberOfEpisodes) {
        super(id, title, labels);
        this.numberOfEpisodes = numberOfEpisodes;
    }

}
```

J'utilise les annotations Lombok pour me sauver du code passe-partout du constructeur et des getters, il sera généré au moment de la construction.

Les vidéos seront créées, supprimés, recherchés dans un service que j'utiliserai dans mon gestionnaire d'API (API handler). Les méthodes de service consistent simplement à gérer le cycle de vie d'une vidéo.

```java
    public String create(Video video) {
        return videoRepository.create(video);
    }

    public String create(Series series) {
        return videoRepository.create(series);
    }

    public String create(Film film) {
        return videoRepository.create(film);
    }

    public void delete(String id) {
        videoRepository.delete(id);
    }

    public ArrayList<Video> getVideosStore() {
        return videoRepository.getVideos();
    }

    public ArrayList<Video> getDeletedVideosStore() {
        return videoRepository.getDeletedVideos();
    }

    public ArrayList<Film> getFilmsStore() {
        return videoRepository.getFilms();
    }

    public ArrayList<Series> getSeriesStore() {
        return videoRepository.getSeries();
    }

    public Video findById(String id) {
        return videoRepository.findById(id);
    }

    public ArrayList<Video> findByTitle(String title) {
        return videoRepository.findByTitle(title);
    }

    public ArrayList<Video> findSimilar(String id, int deg) {
        return videoRepository.findSimilar(id, deg);
    }
```

L' implémentation in-memory du repository a été effectuée dans la classe:
```java
public class InMemoryVideoRepository implements VideoRepository 
```

Afin de pouvoir lancer le projet, il suffit d'utiliser les commandes suivantes :
```bash
mvn clean compile assembly:single
java -jar target/pure-java-recommendation-rest-api-1.0-SNAPSHOT-jar-with-dependencies.jar
```
