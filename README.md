# S8107253Assignment2

Android application for NIT3213 Assignment 2. The app authenticates through the Footscray endpoint and displays dashboard items and their full details.

## Features

- Login with a student ID and first name
- Error messages for invalid input or failed requests
- Dashboard loaded from the API and displayed in a RecyclerView
- Details screen showing every field, including the description
- Dependency injection using Koin
- Unit tests for LoginViewModel and DashboardViewModel

## Build and run

1. Clone this repository and open the `S8107253Assignment2` folder in Android Studio.
2. Allow Gradle to sync and install any required Android SDK components.
3. Run the `app` configuration on an emulator or Android device with internet access.
4. Log in using the student ID **without the `s`** and the case-sensitive first name supplied for the unit.

The app uses `https://nit3213apinew.onrender.com/`, with `POST /footscray/auth` for login and `GET /dashboard/{keypass}` for dashboard items. No credentials are stored in the project.

## Tests

Run the unit tests from Android Studio by right-clicking the `test` folder and selecting **Run Tests**, or run `gradlew.bat test` from the project folder on Windows.

## Tech stack

Kotlin, XML layouts, Retrofit, Moshi, RecyclerView, LiveData/ViewModels, and Koin.