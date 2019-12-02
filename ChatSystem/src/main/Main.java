package main;

import java.util.Date;
import java.sql.Timestamp;

public class Main {

	public static void main(String[] args) {
		
		UserMessage m = new UserMessage("salut godiax");
		int head1 = (int)m.extractType()[0];
		String head2 = new String(m.extractSubtype());
		int head3 = (int)m.extractSize()[0];
		String head4 = new String(m.extractTime());
		System.out.print(head1+head2+head3+head4);

	}

}
