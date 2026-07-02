package org.example;

import org.example.model.entities.FlightRecord;
import org.example.services.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        // Очищаем базу перед каждым тестом (если нужно)
    }

    @Test
    void testGetAllFlights() throws Exception {
        // 1. Подготовка: сохраняем тестовую запись
        FlightRecord record = FlightRecord.builder()
                .latitude(55.7558)
                .longitude(37.6173)
                .altitude(100.0)
                .relativeAltitude(50.0)
                .groundSpeed(10.0)
                .heading(45.0)
                .flightDate(LocalDate.now())
                .build();
        flightService.save(record);

        // 2. Действие: отправляем GET-запрос
        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].latitude").value(55.7558))
                .andExpect(jsonPath("$[0].longitude").value(37.6173));
    }
}
