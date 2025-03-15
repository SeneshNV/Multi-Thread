package com.multi_thread.Multi_Thread.repository;

import com.multi_thread.Multi_Thread.entity.FileDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FileDetailsRepository extends JpaRepository<FileDetailsEntity, Long> {
    Boolean existsByChecksum(String checksum);

    @Query("SELECT MAX(f.id) FROM FileDetailsEntity f")
    Optional<Integer> findMaxId();
}
