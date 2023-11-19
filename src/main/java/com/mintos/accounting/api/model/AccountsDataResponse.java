package com.mintos.accounting.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountsDataResponse {

    private List<AccountDataResponse> accounts;
}
