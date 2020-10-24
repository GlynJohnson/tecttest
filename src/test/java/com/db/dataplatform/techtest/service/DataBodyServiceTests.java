package com.db.dataplatform.techtest.service;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.persistence.repository.DataStoreRepository;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.service.impl.DataBodyServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;

import static com.db.dataplatform.techtest.TestDataHelper.TEST_NAME;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataBodyEntity;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataHeaderEntity;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DataBodyServiceTests {

    public static final String TEST_NAME_NO_RESULT = "TestNoResult";

    @Mock
    private DataStoreRepository dataStoreRepositoryMock;

    private DataBodyService dataBodyService;
    private DataBodyEntity expectedDataBodyEntity;

    @Before
    public void setup() {
        DataHeaderEntity testDataHeaderEntity = createTestDataHeaderEntity(Instant.now());
        expectedDataBodyEntity = createTestDataBodyEntity(testDataHeaderEntity);

        dataBodyService = new DataBodyServiceImpl(dataStoreRepositoryMock);
    }

    @Test
    public void shouldSaveDataBodyEntityAsExpected(){
        dataBodyService.saveDataBody(expectedDataBodyEntity);

        verify(dataStoreRepositoryMock, times(1))
                .save(eq(expectedDataBodyEntity));
    }

    @Test
    public void shouldGetDataByBlockTypeAsExpected(){
        dataBodyService.getDataByBlockType(BlockTypeEnum.BLOCKTYPEA);

        verify(dataStoreRepositoryMock, times(1))
                .findDataByBlockType(eq(BlockTypeEnum.BLOCKTYPEA.name()));
    }

    @Test
    public void shouldGetDataByBlockNameAsExpected(){
        dataBodyService.getDataByBlockName(TEST_NAME);

        verify(dataStoreRepositoryMock, times(1))
                .findDataByName(eq(TEST_NAME));
    }

    @Test
    public void shouldUpdateBlocktypeByNameAsExpected(){
        dataBodyService.updateBlocktypeByName(TEST_NAME, BlockTypeEnum.BLOCKTYPEB.name());

        verify(dataStoreRepositoryMock, times(1))
                .updateBlockTypeByName(eq(TEST_NAME), eq(BlockTypeEnum.BLOCKTYPEB.name()));
    }

}
