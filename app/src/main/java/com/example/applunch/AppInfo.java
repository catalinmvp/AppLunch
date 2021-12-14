package com.example.applunch;

import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

public class AppInfo {
    public ApplicationInfo info;
    public String label;

    @NonNull
    @Override
    public String toString() {
        return label;
    }
}
