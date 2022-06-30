package com.endava.endabank.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantRequestPaginationDto {

    private List<MerchantRequestDataDto> content;
    private Integer totalElements;
    private Integer totalPages;
    private Integer size;
}
