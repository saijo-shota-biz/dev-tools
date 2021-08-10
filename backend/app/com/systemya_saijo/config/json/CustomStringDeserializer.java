package com.systemya_saijo.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

import java.io.IOException;

public class CustomStringDeserializer extends StdDeserializer<String> {

  protected CustomStringDeserializer() {
    super(String.class);
  }

  @Override public String deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException, JsonProcessingException {
    var result = StringDeserializer.instance.deserialize(p, ctxt);
    if (result.isEmpty()) {
      return null;
    }
    return result;
  }
}
