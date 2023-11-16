package com.mintos.accounting.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormattingUtils {

    public static BigDecimal toMoney(BigDecimal from) {
        return from.setScale(2, RoundingMode.UP);
    }
}
