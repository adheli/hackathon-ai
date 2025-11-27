# 🎧 Welcome to Your Mood-Based Music Companion!

This little app is designed to help you discover music that truly matches how you feel.
Just tell it what’s going on — a sentence, a thought, a vibe — and the AI will read the mood behind your words and
suggest emotions that might fit.

Pick the emotion that feels right, choose whether you want songs or albums, and you’ll get a curated list of
recommendations tailored to your emotional moment.

It’s simple, personal, and a fun way to explore music you might’ve never found otherwise.
Whether you’re feeling on top of the world or a bit lost in your thoughts, there’s a soundtrack waiting for you. 🎶💛

---

# 🎵 MoodTunes

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

4. **Call the stateless JSON API**

   The backend exposes REST endpoints rather than serving a web UI:

   - `POST http://localhost:8080/api/analyze` with body `{ "moodText": "..." }` returns a list of suggested emotions.
   - `POST http://localhost:8080/api/recommend` with body `{ "emotion": "...", "mediaType": "SONGS|ALBUMS" }` returns recommendations.

   You can connect any frontend or API client (e.g., Postman, curl, or an SPA) to these endpoints.

---

## 🧪 Running Tests

Run the automated test suite with:

```bash
mvn test
```

### Test Coverage

| Test Class | Purpose |
|-------------|----------|
| `ApiControllerTest` | Verifies JSON endpoints and happy‑path responses |
| `MusicServiceIntegrationTest` | Confirms Spring AI prompt/response flow for recommendations |
| `MoodAnalysisIntegrationTest` | Confirms Spring AI prompt/response flow for emotion extraction |

---

## 🧠 How It Works

1. The user enters a **prompt** that describes how they're feeling in the web interface.
2. The `ApiController` receives the request and delegates to `MoodAnalysisService`.
3. The service builds a structured prompt for ChatGPT
4. The prompt is processed by **Spring AI’s ChatClient**, which calls **OpenAI’s GPT model**.
5. The user selects from the short list of emotions returned, and picks from songs or albums
6. The `ApiController` forwards the user’s selection to the `MusicService`.
7. The generated list of albums or songs is returned to the web page.
8. The response is **cached** (via Caffeine) for faster reuse on similar prompts.

---

## 💾 Caching Behavior

- Uses **Spring Cache** backed by **Caffeine**
- Cache key: `emotion + "_" + mediaType` (e.g., `joy_ALBUMS`)
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


## Stateless API flow (for SPA compatibility)

This project exposes a stateless JSON API:

- `POST /api/analyze` — accepts `{ "moodText": "..." }` and returns `{ "emotions": [...] }`
- `POST /api/recommend` — accepts `{ "moodText":"...", "emotion":"...", "mediaType":"SONGS|ALBUMS" }` and returns `{ "items":[...], "raw":"..." }`

Frontend (Thymeleaf or SPA) sends the mood text and chosen emotion/mediaType — backend keeps no session state.
