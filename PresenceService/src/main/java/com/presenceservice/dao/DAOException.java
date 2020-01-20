package com.presenceservice.dao;

/*
 * Database manipulation exception 
 */
public class DAOException extends RuntimeException {

    public DAOException( String message ) {
        super( message );
    }

    public DAOException( String message, Throwable e ) {
        super( message, e );
    }

    public DAOException( Throwable e ) {
        super( e );
    }
}