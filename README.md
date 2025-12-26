# Movie App

A modern Android application built with Jetpack Compose that showcases "Now Playing" movies using The Movie Database (TMDB) API. This project demonstrates best practices in Android development, including MVVM architecture, clean UI with Material 3, smooth transitions, and dark mode support.

## 🚀 Features

- **Now Playing Movies**: Fetches and displays a grid of the latest movies.
- **Movie Details**: Detailed view for each movie including high-resolution backdrop, overview, release date, and ratings.
- **Modern UI**: Built entirely with Jetpack Compose and Material 3.
- **Shared Element Transitions**: Smooth "hero" animations when navigating from the list to details.
- **Shimmer Loading**: Polished loading state using custom shimmer effects instead of simple progress bars.
- **Dark Mode**: Fully supports system-wide dark mode with theme-aware colors and shimmer.
- **Responsive Design**: Optimized grid layout for different screen sizes.
- **Error Handling**: Graceful network and API error management with retry functionality.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Simple Service Locator (AppContainer)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **JSON Parsing**: [Gson](https://github.com/google/gson)
- **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- **Animations**: Compose Animation (Shared Element Transitions)

## 📦 Project Structure

```text
com.application.movieapp
├── di              # Dependency Injection (AppContainer)
├── model           # Data models (Movie, MovieResponse)
├── network         # API interface (Retrofit)
├── repository      # Data layer handling business logic
├── ui              # UI components and Screens
│   ├── theme       # Material 3 Theme definitions
│   ├── HomeScreen  # Movie listing with Shimmer
│   └── DetailScreen # Movie details with Shared Elements
└── viewmodel       # UI State management
```

## ⚙️ Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/movie-app.git
   ```
2. **Open in Android Studio**: Use the latest version of Android Studio (Ladybug or newer recommended).
3. **API Key**: The project currently uses a hardcoded API key for demonstration. For production, it is recommended to move this to `local.properties`.
4. **Build & Run**: Sync Gradle and run the `app` module on an emulator or physical device.

## 🤝 Contributing

Contributions are welcome! If you'd like to improve the app, please follow these steps:

1. **Fork the Project**.
2. **Create your Feature Branch** (`git checkout -b feature/AmazingFeature`).
3. **Commit your Changes** (`git commit -m 'Add some AmazingFeature'`).
4. **Push to the Branch** (`git push origin feature/AmazingFeature`).
5. **Open a Pull Request**.

### Guidelines

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
- Ensure all new UI components are built with Jetpack Compose.
- Maintain the MVVM architecture.
- Add/Update strings in `strings.xml` (avoid hardcoded strings).
- Verify changes with both Light and Dark themes.

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📞 Contact

Your Name - [@yourhandle](https://twitter.com/yourhandle) - email@example.com

Project Link: [https://github.com/your-username/movie-app](https://github.com/your-username/movie-app)
