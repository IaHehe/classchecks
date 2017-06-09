package com.framework.common.sms.bmob.bson;

public class BSONException extends RuntimeException {
    private static final long serialVersionUID = -19276996871164684L;

    public BSONException() {
    }

    public BSONException(String msg) {
        super(msg, null);
    }

    public BSONException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BSONException(Throwable cause) {
        super(cause == null ? null : cause.toString(), cause);
    }
}
