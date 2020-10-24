package com.db.dataplatform.techtest.client.component.impl;

import com.db.dataplatform.techtest.client.component.Client;
import com.db.dataplatform.techtest.common.model.DataBody;
import com.db.dataplatform.techtest.common.model.DataEnvelope;
import com.db.dataplatform.techtest.common.model.DataHeader;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.util.Arrays;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.db.dataplatform.techtest.TestDataHelper.TEST_NAME;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataEnvelopeApiObject;
import static org.junit.Assert.assertFalse;
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

        DataEnvelope dataEnvelope = createTestDataEnvelopeApiObject();


        try {
            success = client.pushData(dataEnvelope);
        } catch (JsonProcessingException e) {
            log.error("Exception processing data envelope", e);
        }

        assertTrue(success);
    }

    @Test
    public void getDataFromServer() {
        List<DataEnvelope> dataEnvelopes = new ArrayList<>();

        when(restTemplate.getForEntity(any(), any())).thenReturn(generateResponseEntity(HttpStatus.OK));

        dataEnvelopes = client.getData("BLOCKTYPEA");

        assertTrue(dataEnvelopes.size() == 1);


    }

    @Test
    public void givenValidBlockName_thenUpdateDataToServer_isSuccessful() throws UnsupportedEncodingException {
        boolean success = false;
        when(restTemplate.patchForObject(any(), any(), any())).thenReturn(true);

        success = client.updateData(TEST_NAME, BlockTypeEnum.BLOCKTYPEA.name());

        assertTrue(success);
    }

    @Test
    public void givenInvalidName_thenUpdateDataToServer_isNotSuccessful() throws UnsupportedEncodingException {
        boolean success = false;
        when(restTemplate.patchForObject(any(), any(), any())).thenReturn(false);

        success = client.updateData("InvalidName", BlockTypeEnum.BLOCKTYPEA.name());

        assertFalse(success);
    }


    private ResponseEntity<Object> generateResponseEntity(Boolean success, HttpStatus httpStatus) {
        return new ResponseEntity<>(success, httpStatus);
    }

    private ResponseEntity<Object> generateResponseEntity(HttpStatus httpStatus) {

        DataEnvelope testDataEnvelope = createTestDataEnvelopeApiObject();

        return new ResponseEntity<>(Arrays.array(testDataEnvelope), httpStatus);
    }

}