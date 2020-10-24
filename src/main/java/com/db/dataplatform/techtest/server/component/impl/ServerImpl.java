package com.db.dataplatform.techtest.server.component.impl;

import com.db.dataplatform.techtest.common.utils.MD5Checksum;
import com.db.dataplatform.techtest.common.model.DataEnvelope;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.component.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerImpl implements Server {

    private final DataBodyService dataBodyServiceImpl;
    private final ModelMapper modelMapper;

    /**
     * @param envelope
     * @return true if there is a match with the client provided checksum.
     */
    @Override
    public boolean saveDataEnvelope(DataEnvelope envelope, String md5Checksum) throws NoSuchAlgorithmException {
        boolean success = false;
        if (md5Checksum.equals(MD5Checksum.calculateMD5Checksum(envelope.getDataBody().getBody()))) {

            // Save to persistence.
            persist(envelope);

            log.info("Data persisted successfully, data name: {}", envelope.getDataHeader().getName());
            success = true;
        }
        return success;
    }

    private void persist(DataEnvelope envelope) {
        log.info("Persisting data with attribute name: {}", envelope.getDataHeader().getName());
        DataHeaderEntity dataHeaderEntity = modelMapper.map(envelope.getDataHeader(), DataHeaderEntity.class);

        DataBodyEntity dataBodyEntity = modelMapper.map(envelope.getDataBody(), DataBodyEntity.class);
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);

        saveData(dataBodyEntity);
    }

    private void saveData(DataBodyEntity dataBodyEntity) {
        dataBodyServiceImpl.saveDataBody(dataBodyEntity);
    }

    public List<DataEnvelope> getDataByBlockType(String blocktype) {
        List<DataBodyEntity> dataBodyEntities = dataBodyServiceImpl.getDataByBlockType(BlockTypeEnum.valueOf(blocktype));

        return dataBodyEntities.stream().map(DataEnvelope::fromDataBodyEntity).collect(Collectors.toList());
    }

    @Transactional
    public boolean updateBlockName(String name, String newBlockType) {
        Optional<DataBodyEntity> optionalDataBodyEntity = dataBodyServiceImpl.getDataByBlockName(name);

        if (optionalDataBodyEntity.isPresent() &&
                name.equals(optionalDataBodyEntity.get().getDataHeaderEntity().getName())) {
            return dataBodyServiceImpl.updateBlocktypeByName(name, newBlockType) == 1;
        }
        return false;
    }
}
