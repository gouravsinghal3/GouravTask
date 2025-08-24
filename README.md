
✨ **Core Functionality**

**1.Holdings Management:** Comprehensive view of all investment positions
**2.Profit & Loss Tracking:** Daily and total P&L calculations with color-coded indicators
**3.Offline Support:** Local data caching for seamless offline experience

**Note** : Sort and Search CTA and profile icon are just placeholders.

**PROJECT STRUCTURE**

app/
├── src/
│   ├── main/
│   │   ├── java/com/example/gouravtask/
│   │   │   ├── data/
│   │   │   │   ├── api/           # API interfaces and models
│   │   │   │   ├── db/            # Room database and DAOs
│   │   │   │   ├── di/            # Dependency injection modules
│   │   │   │   └── repo/          # Repository implementations
│   │   │   ├── domain/
│   │   │   │   ├── interfaces/    # Repository interfaces
│   │   │   │   └── usecase/       # Business logic use cases
│   │   │   ├── presentation/
│   │   │   │   ├── ui/            # Activities and adapters
│   │   │   │   ├── viewmodel/     # ViewModels
│   │   │   │   └── model/         # UI models and states
│   │   │   └── utils/             # Utility classes and extensions
│   │   ├── res/                   # Resources (layouts, strings, drawables)
│   │   └── AndroidManifest.xml
│   └── test/                      # Unit tests
└── build.gradle.kts

**Technology Stack**

Frontend (Android)
├── Kotlin
├── XML Layouts
├── Material Design 3
└── ViewBinding

Architecture
├── MVVM Pattern
├── Repository Pattern
├── Use Case Pattern
└── Clean Architecture

Backend Integration
├── RESTful API
├── Retrofit + OkHttp
├── JSON Data Format


Local Storage
├── Room Database
├── SQLite
├── Data Caching
└── Offline Support

Dependency Injection
├── Hilt
├── Singleton Components
└── Module-based Architecture

**Design Pattern**
MVVM (Model-View-ViewModel): Clean separation of concerns
Repository Pattern: Centralized data management
Use Case Pattern: Business logic encapsulation

📊 **API Documentation**

**Get Holdings**

http
GET /holdings

**Response** 
{
  "data": {
    "userHolding": [
      {
        "symbol": "AAPL",
        "quantity": 100,
        "ltp": 150.0,
        "avgPrice": 140.0,
        "close": 145.0
      }
    ]
  }
}

**Data Model**
data class NetworkHolding(
    val symbol: String,      // Stock symbol
    val quantity: Int,       // Number of shares
    val ltp: Double,         // Last traded price
    val avgPrice: Double,    // Average purchase price
    val close: Double,       // Previous closing price
)
