package org.repair.model;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringReader;

public class TasksDeserializer extends KeyDeserializer {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public JobTask deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        StringReader stringReader = new StringReader(key);
        return mapper.readValue(stringReader, JobTask.class);
    }
}
