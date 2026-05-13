package com.shop.shop.order.dto;

import com.shop.shop.order.entity.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    // Cart
    @NotNull(message = "Cart ID is required")
    private Long cartId;

    @NotNull(message = "User ID is required")
    private Long userId;

    // Contact Information
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9\\-\\s]{10,15}$", message = "Invalid phone number")
    private String phone;

    // Shipping Address
    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country;

    // Payment
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    // Card Details (required only for online payment)
    private CardDetailsDto cardDetails;

    // Optional
    private String notes;
}