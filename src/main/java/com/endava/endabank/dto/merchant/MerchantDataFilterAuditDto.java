package com.endava.endabank.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDataFilterAuditDto {
    private String storeName;
    private String reviewedByFirstName;
    private String updatedAt;
    private String merchantRequestStateName;
}
