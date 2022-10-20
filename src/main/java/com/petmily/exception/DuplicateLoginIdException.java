package com.petmily.exception;

public class DuplicateLoginIdException extends RuntimeException {

    public DuplicateLoginIdException() {
        super();
    }

    public DuplicateLoginIdException(String message) {
        super(message);
    }

}
