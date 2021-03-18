package com.altertech.evahi.core.connection;

import android.os.AsyncTask;

import okhttp3.Response;

public class Post extends AsyncTask<Void, Void, Response> {

    private IDo
            f;

    private IBefore
            before;

    private ISuccess
            success;

    private IError
            error;

    private IAlways
            always;

    public Post(IDo f) {
        this.f = f;
    }

    public Post before(IBefore l) {
        this.before = l;
        return
                this;
    }

    public Post success(ISuccess l) {
        this.success = l;
        return
                this;
    }

    public Post error(IError l) {
        this.error = l;
        return
                this;
    }

    public Post always(IAlways l) {
        this.always = l;
        return
                this;
    }

    @Override
    protected Response doInBackground(Void... voids) {
        if (this.before != null) {
            this
                    .before.result();
        }
        return this.f.get();
    }

    @Override
    protected void onPostExecute(Response response) {

        if (response.isSuccessful()) {
            if (this.success != null) {

                String
                        data;
                try {
                    data = response.body() != null ? response.body().string() : "";
                } catch (Exception e) {
                    data = "";
                }

                this.success.result(
                        response.code(),
                        data
                );
            }
        } else {
            if (this.error != null) {
                this.error.result(
                        response.code()
                );
            }
        }
        if (this.always != null) {
            this
                    .always.result(response);
        }
    }

    public interface IDo {
        Response get(

        );
    }

    public interface IBefore {
        void result(

        );
    }

    public interface ISuccess {
        void result(
                int code, String data);
    }

    public interface IError {
        void result(
                int code);
    }

    public interface IAlways {
        void result(
                Response response);
    }
}
