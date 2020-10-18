package com.db.dataplatform.techtest.client.component.impl;

import com.db.dataplatform.techtest.client.component.Client;
import com.db.dataplatform.techtest.client.api.model.DataBody;
import com.db.dataplatform.techtest.client.api.model.DataEnvelope;
import com.db.dataplatform.techtest.client.api.model.DataHeader;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientImplTest {

    private static final Logger log = LoggerFactory.getLogger(ClientImplTest.class);

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    Client client = new ClientImpl();

    @Test
    public void pushDataToServer() {
        boolean success = false;
        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn(generateResponseEntity(true, HttpStatus.OK));

        DataEnvelope dataEnvelope = new DataEnvelope(new DataHeader("Name", BlockTypeEnum.BLOCKTYPEA), new DataBody("dataBody"));


        try {
            success = client.pushData(dataEnvelope);
        } catch (JsonProcessingException e) {
            log.error("Exception processing data envelope", e);
        }

        assertTrue(success);
    }

    private ResponseEntity<Object> generateResponseEntity(Boolean success, HttpStatus httpStatus) {
        return new ResponseEntity<>(success, httpStatus);
    }

}