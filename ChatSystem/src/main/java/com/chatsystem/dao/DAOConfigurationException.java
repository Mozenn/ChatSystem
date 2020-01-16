package com.chatsystem.dao;

public class DAOConfigurationException extends RuntimeException {

	
    public DAOConfigurationException( String message ) {
        super( message );
    }

    public DAOConfigurationException( String message, Throwable e ) {
        super( message, e );
    }

    public DAOConfigurationException( Throwable e ) {
        super( e );
    }
}
