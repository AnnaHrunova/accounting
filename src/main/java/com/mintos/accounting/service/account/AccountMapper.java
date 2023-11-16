package com.mintos.accounting.service.account;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.domain.account.AccountEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountData map(AccountEntity accountEntity);
}
