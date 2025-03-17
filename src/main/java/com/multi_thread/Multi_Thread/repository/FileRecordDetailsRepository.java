package com.multi_thread.Multi_Thread.repository;

import com.multi_thread.Multi_Thread.entity.FileRecordDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRecordDetailsRepository extends JpaRepository<FileRecordDetailsEntity, Long> {

    @Query("SELECT MAX(f.lineNumber) FROM FileRecordDetailsEntity f WHERE f.fileDetailsEntity.id = :file_id")
    Optional<Long> findMaxLineNumber(@Param("file_id") Long file_id);
}
