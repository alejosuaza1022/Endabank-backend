package com.endava.endabank.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantRequestDataDto {

    private Integer id;
    private String storeName;
    private String createAt;
    private String merchantRequestStateName;
}
