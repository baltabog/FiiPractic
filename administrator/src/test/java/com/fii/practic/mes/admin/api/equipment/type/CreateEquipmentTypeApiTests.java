package com.fii.practic.mes.admin.api.equipment.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fii.practic.mes.admin.domain.equipment.type.EquipmentTypeService;
import com.fii.practic.mes.admin.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.admin.general.error.ServerErrorEnum;
import com.fii.practic.mes.models.EquipmentTypeDTO;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.restassured.http.Method;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@QuarkusTest
public class CreateEquipmentTypeApiTests {
    public static final String PATH = "/administrator/equipments/types";
    @Inject
    ObjectMapper objectMapper;
    @InjectSpy
    EquipmentTypeService service;

    @Test
    public void testCreateNoContentType() {
        EquipmentTypeDTO inputBodyDto = new EquipmentTypeDTO()
                .name("testType");

        Response requestResponse = given().body(getString(inputBodyDto))
                .headers(Collections.emptyMap())
                .request(Method.POST, PATH);
        String responseBody = requestResponse.getBody().prettyPrint();

        assertEquals(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, requestResponse.getStatusCode(), "Wrong status code. Response body: <%s>".formatted(responseBody));
        assertTrue(StringUtils.isEmpty(responseBody));
    }

    @Test
    public void testCreateNoInputBody() {
        Response requestResponse = given().body("")
                .headers(getHeaders())
                .request(Method.POST, PATH);
        String responseBody = requestResponse.getBody().prettyPrint();

        assertEquals(HttpStatus.SC_BAD_REQUEST, requestResponse.getStatusCode(), "Wrong status code. Response body: <%s>".formatted(responseBody));
        assertTrue(responseBody.contains("\"field\": \"createEquipmentType.equipmentTypeDTO\""));
        assertTrue(responseBody.contains("\"message\": \"must not be null\""));
    }

    @Test
    public void testCreateWithNullName() {
        EquipmentTypeDTO inputBodyDto = new EquipmentTypeDTO();

        Response requestResponse = given().body(getString(inputBodyDto))
                .headers(Collections.emptyMap())
                .request(Method.POST, PATH);
        String responseBody = requestResponse.getBody().prettyPrint();

        assertEquals(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, requestResponse.getStatusCode(), "Wrong status code. Response body: <%s>".formatted(responseBody));
        assertTrue(StringUtils.isEmpty(responseBody));
    }

    @Test
    public void testCreateServiceLogicFailed() {
        EquipmentTypeDTO inputBodyDto = new EquipmentTypeDTO().name("test");
        doThrow(new ApplicationRuntimeException(ServerErrorEnum.UNEXPECTED_EXCEPTION))
                .when(service).create(ArgumentMatchers.any(EquipmentTypeDTO.class));

        Response requestResponse = given().body(getString(inputBodyDto))
                .headers(getHeaders())
                .request(Method.POST, PATH);
        String responseBody = requestResponse.getBody().prettyPrint();

        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, requestResponse.getStatusCode(), "Wrong status code. Response body: <%s>".formatted(responseBody));
        assertTrue(responseBody.contains("\"message\": \"An unexpected exception happened."));

        Timer timer = Metrics.globalRegistry.find("createEquipmentType").timer();
        assertTrue(1 <= timer.count()); // this assert can be improved
    }
    @Test
    public void testCreateServiceLogicPassed() {
        EquipmentTypeDTO inputBodyDto = new EquipmentTypeDTO().name("test");
        doReturn(inputBodyDto
                    .uuid(UUID.randomUUID().toString())
                    .updatedBy("system")
                    .updated(OffsetDateTime.now()))
                .when(service).create(ArgumentMatchers.any(EquipmentTypeDTO.class));

        Response requestResponse = given().body(getString(inputBodyDto))
                .headers(getHeaders())
                .request(Method.POST, PATH);
        String responseBody = requestResponse.getBody().prettyPrint();

        assertDoesNotThrow(() ->
                objectMapper.reader().forType(EquipmentTypeDTO.class).readValue(responseBody));
    }


    private static Map<String, String> getHeaders() {
        return Map.of("Content-Type", "application/json");
    }

    private String getString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <O> O getObject(String jsonString, Class<O> objectType) {
        return objectMapper.convertValue(jsonString, objectType);
    }
}
