package com.db.dataplatform.techtest.api.controller;

import com.db.dataplatform.techtest.TestDataHelper;
import com.db.dataplatform.techtest.common.model.DataEnvelope;
import com.db.dataplatform.techtest.common.utils.MD5Checksum;
import com.db.dataplatform.techtest.server.api.controller.HadoopDummyServerController;
import com.db.dataplatform.techtest.server.api.controller.ServerController;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.exception.HadoopClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.db.dataplatform.techtest.server.api.controller.ServerController.URI_PUSHBIGDATA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class HadoopDummyServerControllerTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private Server serverMock;

    private DataEnvelope testDataEnvelope;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private HadoopDummyServerController hadoopDummyServerController;

    @Before
    public void setUp() throws NoSuchAlgorithmException, IOException {
        hadoopDummyServerController = new HadoopDummyServerController(serverMock);
        mockMvc = standaloneSetup(hadoopDummyServerController).build();
        objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .build();

        testDataEnvelope = TestDataHelper.createTestDataEnvelopeApiObject();

    }

    @Test
    public void testPushDataPostCallWorksAsExpected() throws Exception {

        String testDataEnvelopeJson = objectMapper.writeValueAsString(testDataEnvelope);

        MvcResult mvcResult = mockMvc.perform(post(URI_PUSHBIGDATA)
                .content(testDataEnvelopeJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status == HttpStatus.OK.value() || status == HttpStatus.GATEWAY_TIMEOUT.value()).isTrue();
    }


}