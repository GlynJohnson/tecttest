package com.db.dataplatform.techtest.common.model;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;

import static com.db.dataplatform.techtest.TestDataHelper.DUMMY_DATA;
import static com.db.dataplatform.techtest.TestDataHelper.TEST_NAME;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataBodyEntity;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataHeaderEntity;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DataEnvelopeTests {

    @Test
    public void assignDataHeaderFieldsShouldWorkAsExpected() {
        DataHeader dataHeader = new DataHeader(TEST_NAME, BlockTypeEnum.BLOCKTYPEA);
        DataBody dataBody = new DataBody(DUMMY_DATA);

        DataEnvelope dataEnvelope = new DataEnvelope(dataHeader, dataBody);

        assertThat(dataEnvelope).isNotNull();
        assertThat(dataEnvelope.getDataHeader()).isNotNull();
        assertThat(dataEnvelope.getDataBody()).isNotNull();
        assertThat(dataEnvelope.getDataHeader()).isEqualTo(dataHeader);
        assertThat(dataBody.getBody()).isEqualTo(DUMMY_DATA);
    }

    @Test
    public void fromDataBodyEntityShouldWorkAsExpected() {
        DataHeader dataHeader = new DataHeader(TEST_NAME, BlockTypeEnum.BLOCKTYPEA);
        DataBody dataBody = new DataBody(DUMMY_DATA);

        DataBodyEntity dataBodyEntity = createTestDataBodyEntity(createTestDataHeaderEntity(Instant.now()));

        DataEnvelope dataEnvelope = DataEnvelope.fromDataBodyEntity(dataBodyEntity);

        assertThat(dataEnvelope).isNotNull();
        assertThat(dataEnvelope.getDataHeader()).isNotNull();
        assertThat(dataEnvelope.getDataBody()).isNotNull();
        assertThat(dataEnvelope.getDataHeader()).isEqualToComparingFieldByFieldRecursively(dataHeader);
        assertThat(dataBody.getBody()).isEqualTo(DUMMY_DATA);
    }
}
