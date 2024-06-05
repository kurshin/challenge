# Technical Decisions Document for TastyTrade Application<br/><br/>

### Overview
The TastyTrade application is designed to fetch and display stock quotes based on user searches. Quote sets can be organised into a watchlists. The app incorporates various modern Android development practices and libraries to ensure a robust, maintainable, and scalable application.<br/><br/>

### Architecture
#### MVVM (Model-View-ViewModel) Pattern
Justification:
* Separation of Concerns: MVVM separates the UI logic from business logic, making the codebase more modular and easier to maintain.
* Testability: ViewModels can be tested independently of the UI, improving the test coverage.
* Lifecycle Awareness: ViewModels survive configuration changes, providing a better user experience.<br/><br/>

### Implementation Details
#### Security and Configuration
* Api endpoints are placed on build.gradle.kts of app level. Different environments (development, staging, production) often require different API endpoints. By defining API hosts in build.gradle.kts, one can easily switch between these environments without changing the source code.
* Hardcoding the API key directly in the source code poses a security risk. If the code is pushed to a public repository or shared, the API key could be exposed. Thus, the apiKey property was definet in local.properties. 

#### Dependency Injection with Hilt
Justification:
* Decoupling: Hilt decouples the creation of dependencies from the business logic, leading to cleaner and more modular code.
* Scalability: As the application grows, Hilt makes it easier to manage dependencies without modifying the consumer code.
* Testability: Provides a straightforward way to inject mock dependencies for testing.

#### Networking with Retrofit
Justification:
* Simplicity: Retrofit simplifies the process of making network requests and handling responses.
* Scalability: Retrofit’s modular structure makes it easy to add new endpoints and features.
* Error Handling: Provides robust mechanisms for handling network errors and parsing responses.

#### Asynchronous Programming with Coroutines and LiveData
Justification:
* Main-Safety: Coroutines ensure that long-running operations are not performed on the main thread, keeping the UI responsive.
* Lifecycle Awareness: LiveData is lifecycle-aware, preventing memory leaks and ensuring that UI updates only occur when the UI is active.
* Simplified Syntax: Coroutines provide a more readable and maintainable syntax compared to traditional callback-based approaches.

#### Persistence with Room
Justification:
* Abstraction over SQLite: Room provides a higher-level, more convenient API for database operations compared to SQLite.
* Compile-time Verification: Room verifies SQL queries at compile time, reducing runtime errors.
* Integration with LiveData: Room works seamlessly with LiveData, providing reactive data updates.
