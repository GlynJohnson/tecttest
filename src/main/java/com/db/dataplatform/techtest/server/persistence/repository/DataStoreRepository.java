package com.db.dataplatform.techtest.server.persistence.repository;

import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataStoreRepository extends JpaRepository<DataBodyEntity, Long> {

    @Query(value = "select * from DATA_STORE ds, DATA_HEADER dh where ds.data_header_id=dh.data_header_id and dh.blocktype = :blockType",
            nativeQuery = true)
    List<DataBodyEntity> findDataByBlockType(@Param(value="blockType") String blockType);

    @Query(value = "select * from DATA_STORE ds, DATA_HEADER dh where ds.data_header_id=dh.data_header_id and dh.name = :name",
            nativeQuery = true)
    Optional<DataBodyEntity> findDataByName(@Param(value="name") String name);



}
