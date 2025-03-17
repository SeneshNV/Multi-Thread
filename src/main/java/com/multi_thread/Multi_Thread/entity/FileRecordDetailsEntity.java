package com.multi_thread.Multi_Thread.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileRecordDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private FileDetailsEntity fileDetailsEntity;

    @Column(nullable = false)
    private Long lineNumber;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;
}