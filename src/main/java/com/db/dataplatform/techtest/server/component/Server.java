package com.db.dataplatform.techtest.server.component;

import com.db.dataplatform.techtest.common.model.DataEnvelope;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface Server {
    boolean saveDataEnvelope(DataEnvelope envelope, String md5Checksum) throws IOException, NoSuchAlgorithmException;

    List<DataEnvelope> getDataByBlockType(String blocktype);

    boolean updateBlockName(String name, String newBlockType);
}
