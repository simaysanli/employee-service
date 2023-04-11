package com.justeat.interview.employeeservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justeat.interview.employeeservice.domain.model.EmployeeActionsDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class EmployeeActionsDtoSerializer implements Serializer<EmployeeActionsDto> {

    private static Logger logger = LoggerFactory.getLogger(EmployeeActionsDtoSerializer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, EmployeeActionsDto employeeActionsDto) {
        try {
            if (employeeActionsDto == null) {
                logger.info("Null received at serializing.");
                return null;
            }
            logger.info("Serializing...");
            return objectMapper.writeValueAsBytes(employeeActionsDto);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing employeeActionsDto to byte[]");
        }
    }

    @Override
    public void close() {
    }
}
