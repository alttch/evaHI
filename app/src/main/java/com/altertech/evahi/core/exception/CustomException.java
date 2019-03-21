package com.altertech.evahi.core.exception;

import android.support.annotation.StringRes;

import com.altertech.evahi.R;

/**
 * Created by oshevchuk on 14.02.2019
 */
public class CustomException extends Exception {

    public enum Code {
        BAD_URL(1, R.string.app_exception_invalid_url),
        NO_CONNECTION_TO_SERVER(2, R.string.app_exception_no_connection_to_the_server),
        FILE_NOT_FOUND(3, R.string.app_exception_file_not_found),
        FILE_EMPTY(4, R.string.app_exception_file_empty),
        PARSE_ERROR(5, R.string.app_exception_parse_error),
        PARSE_ERROR_INVALID_SERIAL(6, R.string.app_exception_parse_error_invalid_serial),
        PARSE_ERROR_INVALID_INDEX_PAGE(7, R.string.app_exception_parse_error_invalid_index_page),
        CONNECTION_ERROR_HAND_SHAKE(8, R.string.app_exception_handshake);
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
