# 🎵 AI Music Recommender

A **Spring Boot** web application that uses **Spring AI (ChatGPT)** to suggest music albums based on a user’s text prompt.  
The app provides a simple web interface where users can enter a mood, genre, or activity (e.g., “chill evening” or “energetic workout”), and receive a list of recommended albums.

It also uses **Spring Caching (Caffeine)** to cache AI responses for faster repeated queries.

---

## 🧑‍💻 How to Run

### Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **OpenAI API key**

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/adheli/hackathon-ai.git
   cd hackathon-ai
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   or
   ```bash
   java -jar target/recommender-0.0.1-SNAPSHOT.jar
   ```

4. **Open the app in your browser**
   ```
   http://localhost:8080
   ```

---

## 🧪 Running Tests

Run the automated test suite with:

```bash
mvn test
```

### Test Coverage

| Test Class | Purpose |
|-------------|----------|
| `MusicServiceCacheTest` | Ensures caching avoids redundant AI calls |

---

## 🧠 How It Works

1. The user enters a **prompt** (e.g., “chill evening”, “happy mood”, “focus study”) in the web interface.
2. The `MusicController` sends this to the `MusicService`.
3. The service builds a structured prompt for ChatGPT, for example:
   ```
   You are a helpful music recommendation assistant.
   Suggest 5 music albums that fit: chill evening.
   ```
4. The prompt is processed by **Spring AI’s ChatClient**, which calls **OpenAI’s GPT model**.
5. The generated list of albums is returned to the web page.
6. The response is **cached** (via Caffeine) for faster reuse on similar prompts.

---

## 💾 Caching Behavior

- Uses **Spring Cache** backed by **Caffeine**
- Cache key: `prompt + "_" + count`
- Expiration: **10 minutes**
- Maximum entries: **100**

Example YAML configuration:

```yaml
spring:
  cache:
    type: caffeine
    cache-names: musicRecommendations
    caffeine:
      spec: maximumSize=100,expireAfterWrite=10m
```

---

## 🚨 Error Handling

If something goes wrong (e.g., the AI API fails), users see a friendly **Thymeleaf error page** instead of a raw error message.

- Template: [`error.html`](src/main/resources/templates/error.html)
- Handled by a custom exception handler in the controller layer.

---

## 🧩 Example Usage

| Prompt | Example Output |
|---------|----------------|
| `happy mood` | 5 upbeat pop or indie albums |
| `rainy day jazz` | 5 relaxing jazz albums |
| `focus study` | 5 lo-fi / ambient albums |
| `workout motivation` | 5 energetic rock or electronic albums |

---

## 🧱 Extending the App

You can expand this app by:

- Adding artist or genre filters in `MusicService`
- Supporting different AI models (e.g., `gpt-4o`, `gpt-4-turbo`)
- Logging AI responses for analytics
- Providing a public REST API endpoint

---

## 📄 License

This project is licensed under the **MIT License**.  
See the [LICENSE](LICENSE) file for details.

---

## 🙌 Credits

Built with ❤️ using:

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring AI](https://docs.spring.io/spring-ai/)
- [Caffeine Cache](https://github.com/ben-manes/caffeine)
- [Thymeleaf](https://www.thymeleaf.org/)


## Docker

Build and run with Docker:

```bash
docker build -t ai-music-recommender:latest .
docker run -e OPENAI_API_KEY="${OPENAI_API_KEY}" -p 8080:8080 ai-music-recommender:latest
```

Or with docker-compose:

```bash
docker-compose up --build
```

## CI

A GitHub Actions workflow `.github/workflows/ci.yml` has been added to build and test the project on push/PR.

## Note about legacy endpoint
The old `/recommend` legacy endpoint has been removed in favor of the new multi-step flow under `/flow`:
- POST `/flow/analyze` — analyze mood description and return emotions
- POST `/flow/choose` — user chooses an emotion
- POST `/flow/recommend` — request recommendations (albums or songs)

If you need to keep backward compatibility, we can reintroduce a shim that maps the old behavior to the new services.


## Stateless API flow (for SPA compatibility)

This project exposes a stateless JSON API:

- `POST /api/analyze` — accepts `{ "moodText": "..." }` and returns `{ "emotions": [...] }`
- `POST /api/recommend` — accepts `{ "moodText":"...", "emotion":"...", "mediaType":"SONGS|ALBUMS" }` and returns `{ "items":[...], "raw":"..." }`

Frontend (Thymeleaf or SPA) sends the mood text and chosen emotion/mediaType — backend keeps no session state.

## Docker & CI

A `Dockerfile`, `docker-compose.yml`, and GitHub Actions workflow were added. Build with:

```
docker build -t ai-music-recommender .
docker run -e OPENAI_API_KEY="${OPENAI_API_KEY}" -p 8080:8080 ai-music-recommender
```

Or run tests locally:

```
mvn -DskipTests package
mvn test
```
