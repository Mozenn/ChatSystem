package com.presenceservice.dao;

public class DAOConfigException extends RuntimeException {

	
    public DAOConfigException( String message ) {
        super( message );
    }

    public DAOConfigException( String message, Throwable e ) {
        super( message, e );
    }

    public DAOConfigException( Throwable e ) {
        super( e );
    }
}
