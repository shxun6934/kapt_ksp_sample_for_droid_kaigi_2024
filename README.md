# kapt_ksp_android_sample_project

This project is an Android sample project created for the DroidKaigi 2024 conference. It demonstrates the use of KAPT (Kotlin Annotation Processing Tool) and KSP (Kotlin Symbol Processing) to process annotations and generate code.

## module structure

This project contains the following modules:

- `app`
- `db`
- `view_model_processor`

### `app`

The `app` module is responsible for the Android application and its user interface. It houses the Application class, Activities and ViewModels for each screen, and defines themes for Jetpack Compose.

In the `kotlin1.9/kapt`, `kotlin1.9/ksp`, `kotlin2.0/kapt`, and `kotlin2.0/ksp` branches, the `db` module and the `view_model_processor` module have a dependency relationship.

In the `kotlin1.9/app_only_kapt`, `kotlin1.9/app_ksp_kapt_room`, `kotlin1.9/app_ksp_kapt_hilt`, and `kotlin1.9/app_only_ksp` branches, the project utilizes the Room and Hilt libraries. In these branches, the `db` and `view_model_processor` modules do not have a dependency relationship.

### `db`

The `db` module is responsible for database implementation and configuration. It uses the Room library to handle data operations.

### `view_model_processor`

The `view_model_processor` module defines an Annotation Processor or Symbol Processor. This processor is responsible for defining the `ViewModelFactory` annotation class and generating a corresponding `ViewModelFactory` class.

## Branches

### Kotlin 1.X

- `kotlin1.9/kapt`：Using kapt with Kotlin version 1.9.
- `kotlin1.9/ksp`：Using ksp with Kotlin version 1.9.
- `kotlin1.9/app_only_kapt`：Only using kapt with Kotlin version 1.9 for the `app` module.
- `kotlin1.9/app_ksp_kapt_room`：The `app` module uses both kapt and KSP for annotation processing with Kotlin version 1.9. Kapt is used for Room while KSP is used for Hilt
- `kotlin1.9/app_ksp_kapt_hilt`：The `app` module uses both kapt and KSP for annotation processing with Kotlin version 1.9. KSP is used for Room, while kapt is used for Hilt.
- `kotlin1.9/app_only_ksp`：Only using ksp with Kotlin version 1.9 for the `app` module.

### Kotlin 2.X

- `kotlin2.0/kapt`：Using kapt with Kotlin version 2.0.
- `kotlin2.0/ksp`：Using ksp with Kotlin version 2.0.

## Slides

Coming soon.

## References

### kapt

[kapt Documentation](https://kotlinlang.org/docs/kapt.html)

### KSP

[KSP Overview](https://kotlinlang.org/docs/ksp-overview.html)
