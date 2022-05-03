package com.contoso.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    Сотрудник,
    Админ,
    Менеджер,
    Руководитель;

    @Override
    public String getAuthority() {
        return name();
    }
}
