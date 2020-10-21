package com.db.dataplatform.techtest.common.model;

import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonSerialize(as = DataEnvelope.class)
@JsonDeserialize(as = DataEnvelope.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DataEnvelope {

    @NotNull
    @Valid
    private DataHeader dataHeader;

    @NotNull
    private DataBody dataBody;

    public static DataEnvelope fromDataBodyEntity(DataBodyEntity dataBodyEntity) {

        DataHeader dataHeader = new DataHeader(dataBodyEntity.getDataHeaderEntity().getName(),
                dataBodyEntity.getDataHeaderEntity().getBlocktype());

        DataBody dataBody = new DataBody(dataBodyEntity.getDataBody());

        return new DataEnvelope(dataHeader, dataBody);
    }

}
