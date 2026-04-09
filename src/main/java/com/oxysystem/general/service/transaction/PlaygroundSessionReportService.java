package com.oxysystem.general.service.transaction;


import org.springframework.http.ResponseEntity;

public interface PlaygroundSessionReportService {
    ResponseEntity<?> getPlaygroundSessionReport(String registrationNumber, int page, int size);
}
