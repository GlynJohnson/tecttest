package com.db.dataplatform.techtest.client.component.impl;

import com.db.dataplatform.techtest.client.component.Client;
import com.db.dataplatform.techtest.common.model.DataEnvelope;
import com.db.dataplatform.techtest.common.utils.MD5Checksum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Client code does not require any test coverage
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientImpl implements Client {

    public static final String URI_PUSHDATA = "http://localhost:8090/dataserver/pushdata";
    public static final UriTemplate URI_GETDATA = new UriTemplate("http://localhost:8090/dataserver/data/{blockType}");
    public static final UriTemplate URI_PATCHDATA = new UriTemplate("http://localhost:8090/dataserver/update/{name}/{newBlockType}");

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean pushData(DataEnvelope dataEnvelope) {
        boolean success = false;
        log.info("Pushing data {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Content-MD5", MD5Checksum.calculateMD5Checksum(dataEnvelope.getDataBody().getBody()));
            HttpEntity<DataEnvelope> request =
                    new HttpEntity<>(dataEnvelope, headers);

            ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(URI_PUSHDATA, request, Boolean.class);

            success = responseEntity.getBody() != null ? responseEntity.getBody() : false;

        } catch (NoSuchAlgorithmException e) {
            log.error("Could not find MD5 checksum algorithm", e);
        }


        log.info("Data successfully sent: {}", success);
        return success;
    }

    @Override
    public List<DataEnvelope> getData(String blockType) {
        log.info("Query for data with header block type {}", blockType);

        ResponseEntity<DataEnvelope[]> responseEntity =
                restTemplate.getForEntity(URI_GETDATA.expand(blockType), DataEnvelope[].class);

        if (responseEntity.getBody() != null) {
            DataEnvelope[] dataEnvelopes = responseEntity.getBody();

            log.info("Query for data returned {} blocks", dataEnvelopes.length);
            return Arrays.asList(dataEnvelopes);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean updateData(String blockName, String newBlockType) {
        log.info("Updating blocktype to {} for block with name {}", newBlockType, blockName);

        Boolean isSuccess = restTemplate.patchForObject(URI_PATCHDATA.expand(blockName, newBlockType), null, Boolean.class);

        return isSuccess != null ? isSuccess : false;
    }


}
