package org.cloudfoundry.samples.music.domain;

public class ApplicationInfo {
    private String[] profiles;

    public ApplicationInfo(String[] profiles) {
        this.profiles = profiles;
    }

    public String[] getProfiles() {
        return profiles;
    }

    public void setProfiles(String[] profiles) {
        this.profiles = profiles;
    }
}
