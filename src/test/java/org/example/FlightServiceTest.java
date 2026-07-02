package org.example;

import org.example.model.entities.FlightRecord;
import org.example.services.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlightServiceTest {

    @Autowired
    private FlightService flightService;

    @Test
    void testSaveAndFind() {
        // 1. Подготовка: создаём запись через билдер
        FlightRecord record = FlightRecord.builder()
                .latitude(55.7558)
                .longitude(37.6173)
                .altitude(100.0)
                .relativeAltitude(50.0)
                .groundSpeed(10.0)
                .heading(45.0)
                .flightDate(LocalDate.now())
                .build();

        // 2. Действие: сохраняем
        flightService.save(record);

        // 3. Проверка: ищем по дате и проверяем, что запись есть
        var found = flightService.findByDate(LocalDate.now());
        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(55.7558, found.get(0).getLatitude(), 0.0001);
    }
}