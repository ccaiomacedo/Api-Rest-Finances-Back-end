package com.caiodev.Finances.exception;


public class BusinessRuleException extends RuntimeException{
    private static final long SerialVersionUID = 1L;

    public BusinessRuleException(String msg){
        super(msg);
    }

}
