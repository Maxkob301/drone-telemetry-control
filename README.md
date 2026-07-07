Drone Telemetry & Control System

Drone Telemetry & Control System — это веб-приложение для управления симулятором дрона в реальном времени. Проект демонстрирует навыки разработки на Java, Spring Boot, работы с MAVLink-протоколом, WebSocket и PostgreSQL.

Приложение позволяет:
- Получать телеметрию с симулятора PX4 через MAVLink
- Отображать положение дрона на интерактивной карте в реальном времени
- Сохранять историю полётов в базу данных
- Управлять дроном (взлёт, посадка) через веб-интерфейс
- Просматривать историю полётов с фильтром по дате



Стек технологий

- Язык: Java 17
- Фреймворк: Spring Boot 3.3.2 (Web, Data JPA, WebSocket)
- База данных: PostgreSQL (запускается в Docker)
- Протоколы: MAVLink (через библиотеку dronefleet-mavlink), WebSocket, REST API
- Фронтенд: Thymeleaf, Bootstrap 5, Leaflet (карта), JavaScript
- Инструменты: Maven, Lombok, Docker, Git, GitHub
- Тестирование: JUnit 5, Spring Boot Test, MockMvc



Функциональность

- Телеметрия в реальном времени — данные с дрона обновляются через WebSocket и отображаются на панели и карте.
- Карта с маркером — дрон двигается на карте в реальном времени.
- История полётов — все координаты сохраняются в PostgreSQL и доступны через REST API.
- Управление — кнопки "Взлёт" и "Посадка" отправляют команды через REST API.
- REST API — эндпоинты для получения истории и управления дроном.
- PostgreSQL в Docker — база данных запускается одной командой.



Запуск проекта

1. Клонируй репозиторий

git clone https://github.com/Maxkob301/drone-telemetry-control.git
cd drone-telemetry-control

2. Запусти PostgreSQL в Docker

docker run --name drone-postgres -e POSTGRES_PASSWORD=changeme -p 5433:5432 -d postgres

3. Запусти симулятор PX4 (в WSL)

cd ~/PX4-Autopilot
make px4_sitl jmavsim

4. Запусти Spring Boot приложение

Через IDEA: запусти DroneApplication.main()

Или через Maven:

mvn spring-boot:run

5. Открой браузер

Перейди по адресу: http://localhost:8080



Архитектура проекта

src/main/java/org/example/
├── controller/
│   ├── DroneController.java      # REST API для управления дроном
│   ├── FlightController.java     # REST API для истории полётов
│   └── PageController.java       # Thymeleaf-контроллер страниц
├── mavlink/
│   └── MavlinkConnector.java     # Подключение к симулятору через MAVLink
├── model/
│   ├── Telemetry.java            # DTO для телеметрии
│   └── entities/
│       └── FlightRecord.java     # Сущность для БД
├── repository/
│   └── FlightRecordRepository.java # JPA-репозиторий
├── services/
│   └── FlightService.java        # Бизнес-логика
└── websocket/
    ├── TelemetryWebSocketHandler.java # WebSocket-обработчик
    └── WebSocketConfig.java           # Конфигурация WebSocket



API Эндпоинты

GET /api/flights
Получить все записи истории

GET /api/flights?date=YYYY-MM-DD
Получить записи за дату

POST /api/drone/takeoff
Отправить команду взлёта

POST /api/drone/land
Отправить команду посадки


Лицензия

Этот проект создан в учебных целях. Свободно используй для обучения и портфолио.


Если тебе понравился проект

Поставь звезду на GitHub — это поможет другим разработчикам найти его!
