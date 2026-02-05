# TrainCheck
![TrainCheck Banner](https://user-images.githubusercontent.com/58427823/189905072-de4db750-d7aa-43b4-99a9-196d4e91c709.png)

## Descripción

**TrainCheck** es una aplicación móvil Android desarrollada en **Kotlin** para la gestión integral de clubes de atletismo. Facilita la comunicación entre entrenadores y atletas, centraliza la planificación de entrenamientos y permite hacer seguimiento del estado físico de los deportistas mediante encuestas de sensaciones post-entrenamiento.

## Características principales

### Para entrenadores
- Crear, editar y eliminar entrenamientos para diferentes grupos
- Gestionar asistencia y registrar faltas de los atletas
- Administrar la lista de atletas del club (altas y bajas)
- Visualizar estadísticas de sensaciones y evolución de los deportistas
- Organizar grupos y asignar entrenamientos específicos

### Para atletas
- Consultar entrenamientos asignados con detalles y ejercicios
- Completar encuestas de sensaciones post-entrenamiento
- Visualizar historial de entrenamientos y asistencias
- Acceder a información actualizada en tiempo real

## Stack tecnológico

### Lenguaje y framework
- **Kotlin** - Lenguaje principal de desarrollo
- **Android SDK** - Plataforma nativa Android (minSdk 21, targetSdk 30)

### Arquitectura y patrones
- **MVVM** (Model-View-ViewModel) con AndroidX Lifecycle
- **Navigation Component** - Navegación entre fragmentos
- **Data Binding** - Vinculación de datos con la UI
- **Repository Pattern** - Capa de abstracción de datos (DAL)

### Backend y base de datos
- **Firebase Authentication** - Autenticación de usuarios
- **Firebase Realtime Database** - Base de datos NoSQL en tiempo real
- **Firebase Storage** - Almacenamiento de imágenes y archivos
- **FirebaseUI** - Componentes UI para Auth y Database

### Librerías principales
- **Kotlin Coroutines** - Programación asíncrona
- **AndroidX Core KTX** - Extensiones de Kotlin para Android
- **Material Design Components** - Interfaz de usuario moderna
- **ConstraintLayout** - Layouts flexibles y responsivos
- **Glide** - Carga y caché de imágenes
- **AnyChart** y **MPAndroidChart** - Visualización de estadísticas

### Build system
- **Gradle** con Kotlin DSL
- **Google Services Plugin** - Integración con Firebase

## Estructura del proyecto

```
app/src/main/java/com/finde/android/traincheck/
├── DAL/                    # Data Access Layer - Referencias Firebase
├── Entities/               # Modelos de datos (Athlete, Training, Group, etc.)
├── Fragments/              # Fragmentos de UI organizados por funcionalidad
│   ├── asist/             # Gestión de asistencias
│   ├── main_page/         # Página principal
│   ├── stats/             # Estadísticas y gráficos
│   └── training/          # Entrenamientos
├── ViewModel/              # ViewModels para arquitectura MVVM
├── login/                  # Pantallas de autenticación
└── register/               # Registro de nuevos usuarios
```

## Capturas de pantalla

![App Screenshot](https://user-images.githubusercontent.com/58427823/189907779-365d0eac-5163-4329-be38-3ead931be7f3.jpg)

## Requisitos

- Android 5.0 (API 21) o superior
- Conexión a Internet para sincronización con Firebase
- Cuenta de Firebase configurada con Authentication, Realtime Database y Storage

## Configuración

1. Clonar el repositorio
2. Crear un proyecto en [Firebase Console](https://console.firebase.google.com/)
3. Descargar el archivo `google-services.json` y colocarlo en `app/`
4. Habilitar Authentication (Email/Password) en Firebase
5. Crear Realtime Database con las reglas de seguridad apropiadas
6. Compilar y ejecutar la app en Android Studio

## Tecnologías destacadas

Este proyecto demuestra el uso de:
- Arquitectura limpia y escalable con MVVM
- Integración completa con Firebase ecosystem
- Programación reactiva con Kotlin Coroutines
- Gestión de estados y navegación con Jetpack components
- Visualización de datos con gráficos interactivos

## Autor

**George Sadouski** - [georgesdsk](https://github.com/georgesdsk)

---

*Proyecto desarrollado como TFG (Trabajo de Fin de Grado) para la gestión de clubes deportivos*
