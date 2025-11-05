package com.hubosm.turingsimulator.mappers;

import com.hubosm.turingsimulator.utils.AccountStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AccountStatusConverter implements
        AttributeConverter<AccountStatus, String> {

    @Override
    public String convertToDatabaseColumn(AccountStatus accountStatusEnum) {
        switch (accountStatusEnum){
            case ACTIVE -> {
                return "active";
            }
            case BLOCKED -> {
                return "blocked";
            }
            case NOT_ACTIVATED -> {
                return "not_activated";
            }
        }
        return "not_activated";
    }

    @Override
    public AccountStatus convertToEntityAttribute(String accountStatusString) {
       if(accountStatusString.equals("active")){
           return AccountStatus.ACTIVE;
       }else if(accountStatusString.equals("blocked")){
           return AccountStatus.BLOCKED;
       }else{
           return AccountStatus.NOT_ACTIVATED;
       }
    }
}