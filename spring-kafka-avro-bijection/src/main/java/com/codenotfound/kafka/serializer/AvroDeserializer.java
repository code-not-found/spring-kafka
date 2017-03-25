package com.codenotfound.kafka.serializer;

import java.util.Arrays;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;

public class AvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AvroDeserializer.class);

  protected final Class<T> targetType;

  public AvroDeserializer(Class<T> targetType) {
    this.targetType = targetType;
  }

  @Override
  public void close() {
    // No-op
  }

  @Override
  public void configure(Map<String, ?> arg0, boolean arg1) {
    // No-op
  }

  @SuppressWarnings("unchecked")
  @Override
  public T deserialize(String topic, byte[] data) {
    LOGGER.debug("data to deserialize='{}'", DatatypeConverter.printHexBinary(data));
    try {
      // get the schema
      Schema schema = targetType.newInstance().getSchema();

      Injection<GenericRecord, byte[]> genericRecordInjection = GenericAvroCodecs.toBinary(schema);
      GenericRecord genericRecord = genericRecordInjection.invert((byte[]) data).get();
      T result = (T) SpecificData.get().deepCopy(schema, genericRecord);

      LOGGER.debug("data='{}'", result);
      return result;
    } catch (Exception e) {
      throw new SerializationException(
          "Can't deserialize data [" + Arrays.toString(data) + "] from topic [" + topic + "]", e);
    }
  }
}
