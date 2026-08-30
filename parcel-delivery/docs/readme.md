# Parcel Delivery

Cервис для доставки посылок.

## Архитектура

Система построена по принципу разделения на зоны:

- **API Gateway** — единая точка входа, на которой выполняется аутентификация и маршрутизация запросов c Frontend
- **Parcel Delivery Backend** — зона бэкенда:
    - `Основное приложение (app)` — REST API: CRUD клиентов и доставок, физически располагается в одном деплойменте, выполняет управление изменением статуса (Kotlin, Spring Boot)
    - `Сервис нотификации` — Kafka-consumer: отправка уведомлений клиентам (Kotlin, Spring Boot)
- **Infrastructure** — PostgreSQL, Apache Kafka, ELK, Prometheus

Пользователи (Менеджер, Клиент) → API Gateway → Backend → Infrastructure.

## Структура проекта

```
parcel-delivery/
├── app/                  # Backend-приложение
├── docker/               # Dockerfile
├── docs/                 # Документация
│   ├── 01-biz/           # Бизнес-документация (аудитория, стейкхолдеры, требования)
│   ├── 02-analysis/      # Функциональные и нефункциональные требования
│   └── architecture.puml # Диаграмма С4
└── settings.gradle.kts
```

### Планируемые модули

- `common` — общие компоненты
- `lib-*` — библиотечные модули

## Сборка

```bash
# Все модули parcel-delivery
./gradlew :parcel-delivery:build

# Модуль app
./gradlew :parcel-delivery:app:build
```

## Docker

Сборка образа (из корня `kotlin-educational-project/`):

```bash
docker build -f parcel-delivery/docker/Dockerfile -t parcel-delivery:latest ./
```

Подробнее — в [parcel-delivery/docker/README.md](../docker/README.md).

## Инструменты сопровождения: ELK и Prometheus

Для логирования применяется ELK 7.x, для мониторинга - Prometheus. Запуск инфраструктуры из корня проекта:

```bash
docker-compose up -d
```

| Сервис        | Порт | Назначение             |
|---------------|------|------------------------|
| Elasticsearch | 9200 | Хранение и поиск логов |
| Logstash      | 9600 | Приём логов            |
| Kibana        | 5601 | Визуализация  логов    |
| Prometheus    | 9090 | Сбор метрик            |
| Grafana       | 3000 | Визуализация метрик    |

## Convention Plugins

Все модули используют convention plugins из `gradle-plugins`:
- `jvm-convention` — для JVM модулей
- `multiplatform-convention` — для Kotlin Multiplatform модулей