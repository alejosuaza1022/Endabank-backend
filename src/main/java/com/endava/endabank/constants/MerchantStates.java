package com.endava.endabank.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MerchantStates {
    public static final Integer PENDING = 1;
    public static final Integer APPROVED = 2;
    public static final Integer REJECTED = 3;

}
