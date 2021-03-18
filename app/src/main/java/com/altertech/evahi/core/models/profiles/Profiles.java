package com.altertech.evahi.core.models.profiles;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Profiles {

    public List<Profile> profiles = new ArrayList<>();

    public Profiles(

    ) {
    }

    public Profiles(
            List<Profile> profiles) {
        this();
        this.profiles = profiles;
    }

    public Profiles add(Profile profile) {
        this.profiles
                .add(profile);
        return this;
    }

    public Profile get(Long id) {
        for (Profile profile : this.profiles) {
            if (profile.id != null && profile.id.equals(id)) {
                return
                        profile;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Profiles{" +
                "profiles=" + profiles +
                '}';
    }
}