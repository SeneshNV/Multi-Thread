package com.multi_thread.Multi_Thread.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileCode;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date uploadAt;

    @Column(nullable = false)
    private String checksum;

    @Column(nullable = false)
    private String status;

}
