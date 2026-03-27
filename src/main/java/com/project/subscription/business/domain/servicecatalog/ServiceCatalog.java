package com.project.subscription.business.domain.servicecatalog;

import lombok.Getter;

@Getter
public enum ServiceCatalog {

    NETFLIX(1L),
    YOUTUBE(2L);

    private final Long serviceId;

    ServiceCatalog(Long serviceId) {
        this.serviceId = serviceId;
    }

}
