package com.db.dataplatform.techtest.service;

import com.db.dataplatform.techtest.common.utils.MD5Checksum;
import com.db.dataplatform.techtest.common.model.DataEnvelope;
import com.db.dataplatform.techtest.server.mapper.ServerMapperConfiguration;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.component.impl.ServerImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.db.dataplatform.techtest.TestDataHelper.TEST_NAME;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataEnvelopeApiObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServerServiceTests {

    @Mock
    private DataBodyService dataBodyServiceImplMock;

    private ModelMapper modelMapper;

    private DataBodyEntity expectedDataBodyEntity;
    private DataEnvelope testDataEnvelope;

    private String md5Checksum;

    private Server server;

    @Before
    public void setup() throws NoSuchAlgorithmException {
        ServerMapperConfiguration serverMapperConfiguration = new ServerMapperConfiguration();
        modelMapper = serverMapperConfiguration.createModelMapperBean();

        testDataEnvelope = createTestDataEnvelopeApiObject();
        expectedDataBodyEntity = modelMapper.map(testDataEnvelope.getDataBody(), DataBodyEntity.class);
        expectedDataBodyEntity.setDataHeaderEntity(modelMapper.map(testDataEnvelope.getDataHeader(), DataHeaderEntity.class));

        md5Checksum = MD5Checksum.calculateMD5Checksum(expectedDataBodyEntity.getDataBody());

        server = new ServerImpl(dataBodyServiceImplMock, modelMapper);


    }

    @Test
    public void shouldSaveDataEnvelopeAsExpected() throws NoSuchAlgorithmException, IOException {
        boolean success = server.saveDataEnvelope(testDataEnvelope, md5Checksum);

        assertThat(success).isTrue();
        //verify(dataBodyServiceImplMock, times(1)).saveDataBody(eq(expectedDataBodyEntity));
    }

    @Test
    public void shouldGetDataByType() throws NoSuchAlgorithmException, IOException {
        DataEnvelope expectedDataEnvelope = createTestDataEnvelopeApiObject(TEST_NAME);

        when(dataBodyServiceImplMock.getDataByBlockType(BlockTypeEnum.BLOCKTYPEA))
                .thenReturn(Collections.singletonList(expectedDataBodyEntity));

        List<DataEnvelope> dataEnvelopes = server.getDataByBlockType(BlockTypeEnum.BLOCKTYPEA.name());

        assertThat(dataEnvelopes.get(0)).isEqualToComparingFieldByFieldRecursively(expectedDataEnvelope);
    }

    @Test
    public void shouldUpdateBlockName() throws NoSuchAlgorithmException, IOException {
        when(dataBodyServiceImplMock.getDataByBlockName(TEST_NAME)).thenReturn(Optional.of(expectedDataBodyEntity));
        when(dataBodyServiceImplMock.updateBlocktypeByName(TEST_NAME, BlockTypeEnum.BLOCKTYPEB.name())).thenReturn(1);

        boolean success = server.updateBlockName(TEST_NAME, BlockTypeEnum.BLOCKTYPEB.name());

        assertThat(success).isTrue();
    }

    @Test
    public void shouldntUpdateBlockName() throws NoSuchAlgorithmException, IOException {
        when(dataBodyServiceImplMock.getDataByBlockName(any())).thenReturn(Optional.empty());

        boolean success = server.updateBlockName(TEST_NAME, BlockTypeEnum.BLOCKTYPEB.name());

        assertThat(success).isFalse();
    }


}
