package com.example.demo.model.wallet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WalletBalance {
    private int userId;
    private int currencyId;
    private String currencyName;
    private String currencySymbol;
    private BigDecimal balance;
}
