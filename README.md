# Веб-сервис для хранения паролей

## Стек

Java 21, Spring Boot, Postgres 16, Docker, Swagger UI,
Hibernate Envers - для поддержки истории изменений,
Apache Commons CSV - для работы с CSV.

## Запуск

`docker compose up`

## Swagger

Доступен по [адресу](http://localhost:8080/swagger-ui.html).

## Notes

Не было времени заняться статус-кодами,
обработкой ошибок и написанием тестов.

Для импорта из csv запрос в Postman'е выглядит так:

![img](/example/exampleCSVimport.png)
