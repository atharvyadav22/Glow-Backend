package org.aystudios.Skincare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private int status;
    private String code;
    private String message;
    private String path;
    private LocalDateTime timestamp;

}
