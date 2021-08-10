package com.systemya_saijo.config.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.Provider;
import com.systemya_saijo.core.domain.vo.StringValueObject;
import play.libs.Json;

public class CustomObjectMapper implements Provider<ObjectMapper> {
  @Override
  public ObjectMapper get() {
    ObjectMapper mapper = new ObjectMapper();

    SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1, 0, 0, "", "", ""));
    simpleModule.addSerializer(StringValueObject.class, new StringValueObjectSerializer());
    simpleModule.addDeserializer(String.class, new CustomStringDeserializer());

    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.registerModule(simpleModule);

    // Needs to set to Json helper
    Json.setObjectMapper(mapper);

    return mapper;
  }
}
