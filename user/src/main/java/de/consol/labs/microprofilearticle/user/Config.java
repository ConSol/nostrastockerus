package de.consol.labs.microprofilearticle.user;

import de.consol.labs.microprofilearticle.common.config.AbstractConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Config extends AbstractConfig {

    @Inject
    @ConfigProperty(name = "de.consol.labs.microprofilearticle.user.config.adminTokenPath", defaultValue = "/app/admin-token.txt")
    private String adminTokenPath;

    public String getAdminTokenPath() {
        return adminTokenPath;
    }
}
