package dev.hireben.user_management.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonPropertyOrder({ "field", "message" })
final class FieldValidationErrorMap {
  String field;
  String message;
}
