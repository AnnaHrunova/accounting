package com.mintos.accounting.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StringSearchCriteria {
  private String field;
  private String operator;
  private String value;
}
