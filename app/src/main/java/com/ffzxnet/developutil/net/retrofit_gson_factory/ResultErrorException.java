package com.ffzxnet.developutil.net.retrofit_gson_factory;

import java.io.IOException;

/**
 * 创建者： feifan.pi 在 2017/3/22.
 */

public class ResultErrorException extends IOException {

    public ResultErrorException() {
        super();
    }

    public ResultErrorException(String message) {
        super(message);
    }

    public ResultErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultErrorException(Throwable cause) {
        super(cause);
    }
}
