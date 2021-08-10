package com.systemya_saijo.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.systemya_saijo.core.domain.vo.StringValueObject;

import java.io.IOException;

public class StringValueObjectSerializer extends StdSerializer<StringValueObject> {
  protected StringValueObjectSerializer() {
    super(StringValueObject.class);
  }

  @Override public void serialize(StringValueObject value, JsonGenerator gen, SerializerProvider provider)
    throws IOException {
    gen.writeString(value.getValue());
  }
}
