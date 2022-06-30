package com.endava.endabank.dto.transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayTransactionCreatedDto {
    private Double amount;
    private String stateType;
    private String stateDescription;
    private String merchant;
    private String bankAccountIssuer;
    private Integer id;
    private String createAt;
    private String description;
}
