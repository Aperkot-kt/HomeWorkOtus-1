# Lessons Modules

Композитный модуль для учебных материалов.

## Структура проекта

- `submodule-1` - материалы 1-ого урока 1-ого модуля

## Создание нового модуля

1. Создайте новую директорию:
   ```bash
   mkdir -p lessons-modules/submodule-новый-номер/src/main/kotlin/ru/otus/kotlin
   ```

2. Создайте `build.gradle.kts`:
   ```kotlin
   plugins {
       id("jvm-convention")  // или multiplatform-convention для KMP
   }

   // Добавление специфичных зависимостей:
   dependencies {
        implementation("example:example:1.0.0")
   }
   ```

3. Добавьте модуль в `lessons-modules/settings.gradle.kts`:
   ```kotlin
   include("submodule-новый-номер")
   ```

4. Соберите модуль:
   ```bash
   ./gradlew :lessons-modules:submodule-новый-номер:build
   ```

## Convention Plugins

Все модули  `jvm-convention` plugin для JVM модулей.
Это обеспечивает единообразную конфигурацию и упрощает поддержку.

## Сборка

```bash
# Все модули lessons-modules
./gradlew :lessons-modules:build

# Конкретный модуль
./gradlew :lessons-modules:submodule-новый-номер:build
```