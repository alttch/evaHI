package com.altertech.evahi.ui.base;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.altertech.evahi.ui.base.i.IACallBack;
import com.altertech.evahi.ui.holders.view.VHBase;


public class ABase<APP extends Application> extends AppCompatActivity implements IACallBack {

    protected APP app;

    protected VHBase
            h;

    @Override
    protected void onCreate(Bundle instance) {

        super
                .onCreate(instance);

        this
                .setContentView(this.getLayout());

        this.h = new VHBase(this.findViewById(android.R.id.content));

        this.app = (APP) this.getApplicationContext();

        this.init();
        this
                .created();
    }

    @Override
    protected void onResume() {
        super
                .onResume();
        this.resume();
    }

    @Override
    public void onPause() {
        super
                .onPause();
        this.pause();
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    @Override
    protected void onActivityResult(
            int request, int result, Intent data) {
        super.onActivityResult(
                request, result, data);
        this.result(request, result, data);
    }

    @Override
    public void onRequestPermissionsResult(
            int request, @NonNull String[] permissions, @NonNull int[] results) {
        this.permissions(
                request, permissions, results);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(
                config);
        this
                .configuration(config);
    }

    protected @LayoutRes
    int getLayout() {
        return
                -1;
    }

    /*-------------------------------------------------------------------------------*/

    @Override
    public void init() {

    }

    @Override
    public void created() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void back() {
        this
                .finish();
    }

    @Override
    public void result(
            int request, int result, Intent data) {

    }

    @Override
    public void permissions(
            int request, @NonNull String[] permissions, @NonNull int[] results) {

    }

    @Override
    public void configuration(Configuration config) {

    }

    /*-------------------------------------------------------------------------------*/

    public <T> T data(String key, Class<T> cls) {
        return data(
                this.getIntent(), key, cls);
    }

    public <T> T data(String key, Class<T> cls, T def) {
        return data(
                this.getIntent(), key, cls, def);
    }

    public <T> T data(Intent intent, String key, Class<T> cls) {
        return
                data(intent, key, cls, null);
    }

    public <T> T data(Intent intent, String key, Class<T> cls, T def) {
        if (intent != null) {
            if (intent.getSerializableExtra(key) != null) {
                return
                        cls.cast(intent.getSerializableExtra(key));
            }
        }
        return def;
    }

    /*-------------------------------------------------------------------------------*/

    public ABase<APP> result(int result) {
        return this.result(result, null);
    }

    public ABase<APP> result(int result, Intent data) {
        this
                .setResult(result, data);
        return this;
    }
}
