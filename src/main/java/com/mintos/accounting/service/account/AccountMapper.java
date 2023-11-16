package com.mintos.accounting.service.account;

import com.mintos.accounting.domain.account.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountData map(AccountEntity accountEntity);
}
