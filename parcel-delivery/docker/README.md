# Docker — Parcel Delivery

## Сборка образа

Команда сборки образа запускается из корня проекта `kotlin-educational-project/`:

```bash
docker build -f parcel-delivery/docker/Dockerfile -t parcel-delivery:latest ./
```

## Запуск контейнера

```bash
docker run -p 8080:8080 parcel-delivery:latest
```

## Запуск инфраструктуры

1. Сначала поднимите инфраструктуру из корня проекта командой

```bash
docker-compose up -d
```

2. Запустите приложение командой

```bash
docker run --network kotlin-educational-project_infra -p 8080:8080 parcel-delivery:latest
```

## Порты

| Сервис          | Порт |
|-----------------|------|
| Elasticsearch   | 9200 |
| Logstash        | 9600 |
| Kibana          | 5601 | 
| Prometheus      | 9090 |
| Grafana         | 3000 |
| Parcel Delivery | 8080 | 