# Parcel Delivery

Композитный модуль для проекта parcel-delivery.

## Структура проекта

- `parcel-delivery-api` - API и контракты (заглушка для примера)

### Планируемые модули

- `parcel-delivery-common` - Общие компоненты
- `parcel-delivery-backend` - Основное приложение
- `parcel-delivery-lib-*` - Библиотечные модули

## Создание нового модуля

1. Создайте директорию для модуля:
   ```bash
   mkdir -p parcel-delivery/parcel-delivery-новый-модуль/src/main/kotlin/ru/parceldelivery/новый-пакет
   mkdir -p parcel-delivery/parcel-delivery-новый-модуль/src/test/kotlin/ru/parceldelivery/новый-пакет
   ```

2. Создайте `build.gradle.kts`:
   ```kotlin
   plugins {
       id("jvm-convention")  // или multiplatform-convention для KMP
   }

   // Добавление специфичных зависимостей:
   dependencies {
       implementation(project(":parcel-delivery-common"))
   }
   ```

3. Добавьте модуль в `parcel-delivery/settings.gradle.kts`:
   ```kotlin
   include("parcel-delivery-новый-модуль")
   ```

4. Соберите модуль:
   ```bash
   ./gradlew :parcel-delivery:parcel-delivery-новый-модуль:build
   ```

## Convention Plugins

Все модули используют convention plugins из `gradle-plugins`:
- `jvm-convention` - для JVM модулей (сейчас: parcel-delivery-api)
- `multiplatform-convention` - для Kotlin Multiplatform модулей (будущие модули)

Это обеспечивает единообразную конфигурацию и упрощает поддержку.

## Сборка

```bash
# Все модули parcel-delivery
./gradlew :parcel-delivery:build

# Конкретный модуль
./gradlew :parcel-delivery:parcel-delivery-api:build
```