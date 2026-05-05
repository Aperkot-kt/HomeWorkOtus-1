# Kotlin Educational Project

Учебный проект для курса OTUS Kotlin Backend Developer.

## Структура проекта

Проект организован в виде трех композитных модулей (composite builds):

### 1. lessons-modules
Учебные материалы курса.
- `submodule-1` - Материалы первого урока

### 2. parcel-delivery
Персональный проект (модуль доставки посылок).
- `parcel-delivery-api` - Модуль-заглушка (пример структуры)

### 3. gradle-plugins
Общие Gradle плагины для переиспользования настроек сборки.

## Сборка проекта

```bash
# Просмотр всех проектов
./gradlew projects

# Сборка всех модулей
./gradlew build

# Сборка конкретного модуля
./gradlew :lessons-modules:submodule-1:build
./gradlew :parcel-delivery:build
./gradlew :gradle-plugins:build
```

## Требования

- JDK 21
- Gradle 8.10
- Kotlin 2.2.21