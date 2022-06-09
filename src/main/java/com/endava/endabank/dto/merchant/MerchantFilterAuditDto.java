package com.endava.endabank.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantFilterAuditDto {
    private String merchantName;
    private String adminName;
    private String startDate;
    private String endDate;
}
