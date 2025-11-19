package dev.hireben.user_management.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class UserDTOTests {

  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  private static final ObjectMapper objectMapper = new ObjectMapper();

}
