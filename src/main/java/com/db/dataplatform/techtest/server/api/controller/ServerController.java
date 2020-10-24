package com.db.dataplatform.techtest.server.api.controller;

import com.db.dataplatform.techtest.common.model.DataEnvelope;
import com.db.dataplatform.techtest.server.component.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/dataserver")
@RequiredArgsConstructor
@Validated
public class ServerController {

    private final Server server;

    @PostMapping(value = "/pushdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pushData(@Valid @RequestBody DataEnvelope dataEnvelope,
                                            @RequestHeader("Content-MD5") String md5Checksum) throws IOException, NoSuchAlgorithmException {

        log.info("Data envelope received: {}", dataEnvelope.getDataHeader().getName());
        boolean checksumPass = server.saveDataEnvelope(dataEnvelope, md5Checksum);

        log.info("Data envelope persisted. Attribute name: {}", dataEnvelope.getDataHeader().getName());
        return ResponseEntity.ok(checksumPass);
    }

    @GetMapping(value="/data/{blockType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DataEnvelope>> queryData(@PathVariable("blockType") String blockType) {
        List<DataEnvelope> dataBlocks;

        dataBlocks = server.getDataByBlockType(blockType);

        return new ResponseEntity<>(dataBlocks, HttpStatus.OK);
    }

    @PatchMapping(value="/update/{name}/{newBlockType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> patchData(@PathVariable("name") String name, @PathVariable("newBlockType") String newBlockType) {
        boolean isSuccess;

        isSuccess = server.updateBlockName(name, newBlockType);

        log.info("Block name {}{} updated to block type {}", name, isSuccess ? "" : " not", newBlockType);

        return new ResponseEntity<>(isSuccess, HttpStatus.OK);
    }

}
