package com.velo.courrier.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FeatureFlagService {

    @Value("${features.multi_stop.backend_enabled:false}")
    private boolean multiStopBackendEnabled;

    @Value("${features.multi_stop.admin_override_enabled:false}")
    private boolean multiStopAdminOverrideEnabled;

    public boolean isMultiStopBackendEnabled() {
        return multiStopBackendEnabled;
    }

    public boolean isMultiStopAdminOverrideEnabled() {
        return multiStopAdminOverrideEnabled;
    }
}
