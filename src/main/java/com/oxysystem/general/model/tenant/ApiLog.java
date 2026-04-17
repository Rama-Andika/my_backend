package com.oxysystem.general.model.tenant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "api_logs")
public class ApiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "api_log_id")
    private Long apiLogId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Column(name = "req_name")
    private String reqName;

    @Column(name = "req_header")
    private String reqHeader;

    @Column(name = "req_body", columnDefinition = "TEXT")
    private String reqBody;

    @Column(name = "res_body", columnDefinition = "TEXT")
    private String resBody;
}
