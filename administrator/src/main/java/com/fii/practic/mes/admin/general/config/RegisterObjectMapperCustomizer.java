// Copyright by camLine GmbH, Germany, 2022
package com.fii.practic.mes.admin.general.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;


@Singleton
public class RegisterObjectMapperCustomizer implements ObjectMapperCustomizer {

	@Override
	public void customize(final ObjectMapper objectMapper) {
		objectMapper
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.configure(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION, true)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
				.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
				.registerModule(new JavaTimeModule());
	}
}
