package mware_lib;

import java.io.IOException;

public interface IConnection {
	public Object sendReceive(Object o) throws IOException;
	public String getLocalAddress();
}
