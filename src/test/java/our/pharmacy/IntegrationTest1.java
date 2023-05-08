package our.pharmacy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import our.pharmacy.dto.MedicineDto;
import our.pharmacy.dto.MedicineFilter;

import static our.pharmacy.utils.testcontainers.JsonTestUtils.toJson;

@our.pharmacy.utils.IntegrationTest
public class IntegrationTest1 {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Sql("classpath:db/insert_medicine.sql")
    public void test1() {
        MedicineFilter medicineFilter = new MedicineFilter(0, 10, "Ал", 200L, 1000L, null, MedicineFilter.SortOrder.ASC, MedicineFilter.SortField.PRICE);
        var j = toJson(medicineFilter);


        webTestClient.post()
                .uri("/medicine/guide")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(j)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.medicines[0].price").isEqualTo(298)
                .jsonPath("$.medicines[1].price").isEqualTo(891)
                .jsonPath("$.medicines[2]").doesNotExist();
    }
    @Test
    public void test2() {
        MedicineDto medicineDto = new MedicineDto(null, "Лекарство", "3456", "Описание", null, 2L, true);
        MedicineDto medicineDto2 = new MedicineDto(1L, "Лекарство 2", "3456", "Описание", null, 2L, false);

        var j1 = toJson(medicineDto);
        var j2 = toJson(medicineDto2);

        webTestClient.post()
                .uri("/medicine")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(j1)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/medicine") .queryParam("id", "1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1L);

        webTestClient.put()
                .uri("/medicine")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(j2)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/medicine") .queryParam("id", "1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo("Лекарство 2");
    }

    @Test
    @Sql("classpath:db/insert_medicine.sql")
    public void test3() {
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/medicine") .queryParam("id", "1")
                        .build())
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/medicine") .queryParam("id", "1")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
