package com.chatsystem.session;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import com.chatsystem.model.SystemContract;
import com.chatsystem.user.User;

public class DistantSession extends Session {
	
	private ServerSocket socket ; 
	
	public DistantSession(User emitter, User receiver, SystemContract system) throws IOException
	{
		super(emitter,receiver,system) ; 
		startSession() ; 
	}
	
	public DistantSession(User emitter, User receiver, int receiverPort , SystemContract system) throws IOException
	{
		super(emitter,receiver,receiverPort,system) ; 
		startSession() ; 
	}

	@Override
	protected void startSession() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyStartSession() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyStartSessionResponse(Object packetReceived) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyCloseSession() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void closeSession() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void sendMessage(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(File f) {
		// TODO Auto-generated method stub

	}

}
