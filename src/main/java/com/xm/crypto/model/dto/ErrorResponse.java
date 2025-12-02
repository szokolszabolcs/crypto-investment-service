package com.xm.crypto.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse", description = "Response containing details when an error occurred")
public record ErrorResponse(
        @Schema(description = "Custom error code identifying the error", example = "CUSTOM_ERROR")
        ErrorCode errorCode,
        @Schema(description = "Error message containing details about the error", example = "Error message.")
        String message
) {
}
