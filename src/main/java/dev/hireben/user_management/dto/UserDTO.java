package dev.hireben.user_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserDTO(
    @NotBlank(message = "Missing email input") String name,
    @NotBlank(message = "Missing email input") String username,
    @NotBlank(message = "Email is required") @Email(message = "Must be a valid email") String email,
    @NotBlank(message = "Phone cannot be blank") @Pattern(regexp = "^[0-9]{10}$", message = "Phone number is invalid") String phone,
    @NotBlank(message = "Website cannot be blank") @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://") String website) {
}
