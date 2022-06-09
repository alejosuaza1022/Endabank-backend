package com.endava.endabank.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantRegisterDto {
    private Integer id;

    @NotNull
    @Size(min = 10, max = 11)
    private String taxId;

    @NotNull
    @NotBlank
    private String address;

    @NotNull
    @NotBlank
    private String storeName;
}
