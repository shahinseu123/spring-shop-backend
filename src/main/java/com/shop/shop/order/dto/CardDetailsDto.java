package com.shop.shop.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailsDto {

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^[0-9\\s]{16,19}$", message = "Invalid card number")
    private String cardNumber;

    @NotBlank(message = "Card holder name is required")
    private String cardName;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Invalid expiry date format (MM/YY)")
    private String expiryDate;

    @NotBlank(message = "CVV is required")
    @Size(min = 3, max = 4, message = "CVV must be 3 or 4 digits")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid CVV")
    private String cvv;
}
