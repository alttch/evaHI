package com.altertech.evacc.core.exception;

import android.support.annotation.StringRes;

import com.altertech.evacc.R;

/**
 * Created by oshevchuk on 14.02.2019
 */
public class CustomException extends Exception {

    public enum Code {
        BAD_URL(1, R.string.app_exception_bad_url),
        NO_CONNECTION_TO_SERVER(2, R.string.app_exception_no_connection_to_the_server),
        FILE_NOT_FOUND(3, R.string.app_exception_file_not_found),
        FILE_EMPTY(4, R.string.app_exception_file_empty),
        PARSE_ERROR(5, R.string.app_exception_parse_error);
        int id;
        @StringRes
        int message;

        Code(int id, int message) {
            this.id = id;
            this.message = message;
        }

        public int getId() {
            return id;
        }

        public int getMessage() {
            return message;
        }
    }

    private Code code;

    public CustomException(Code code) {
        this.code = code;
    }


    public Code getCode() {
        return code;
    }


}
