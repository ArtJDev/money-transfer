## Кейс "Сервис перевода денег"
### Стек: Java 17, Spring Boot 3.1.x, Spring Data JDBC, PostgreSQL, Flyway, Apache Commons Lang, JUnit, Maven, Docker.
Приложение представляет собой сервис по созданию счетов, перевода средств между счетами, снятие и внесение денежных средств на счета.
В приложении реализовано:
- просмотр существующих счетов;
- создание нового счета;
- перевод со счета на счет;
- снятие средств со счета;
- пополнение счета;

Написаны unit тесты на сервисный слой.

Для работы приложения необходима база данных **PostgreSQL**, которую можно поднять в **Docker** контейнере, воспользовавшись приложенным в корне проекта файлом **docker-compose.yml**, написав в терминале команду `docker-compose up -d`.

Запустить приложение можно командой из терминала `./mvnw spring-boot:run`

Остановить приложение - нажать Ctrl + C.

При запуске в приложении создаются два тестовых аккаунта счета.

После запуска приложения можно посмотреть ендпоинты контроллера, схему запросов, спецификацию и протестировать работу перейдя по ссылке http://localhost:9090/swagger-ui/index.html
