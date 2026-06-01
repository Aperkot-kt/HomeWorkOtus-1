# Parcel Delivery API

Модуль-заглушка для демонстрации структуры проекта.

## Структура

```
parecel-delivery-api/
├── build.gradle.kts                    # Конфигурация сборки модуля
├── README.md                           # Описание модуля
└── src/
    ├── main/
    │   └── kotlin/
    │       └── ru/parceldelivery/api/
    │           └── Stub.kt          # Пример кода
    └── test/
        └── kotlin/                     
            │       └── ru/parceldelivery/api/
            │           └── StubTest.kt          # Пример теста
```

## Создание нового модуля

Чтобы создать новый модуль в parcel-delivery:

1. Создайте директорию с именем модуля в `parcel-delivery/`
2. Скопируйте `build.gradle.kts` из этого модуля
3. Создайте структуру `src/main/kotlin/`
4. Добавьте `include("имя-модуля")` в `parcel-delivery/settings.gradle.kts`

## Сборка

```bash
# Из корня проекта
./gradlew :parcel-delivery:parcel-delivery-api:build

# Из директории parcel-delivery
cd parcel-delivery
../gradlew parcel-delivery-api:build
```