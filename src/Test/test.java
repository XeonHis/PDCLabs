package Test;

import Tools.JDBCUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;

/**
 * @author paulalan
 * @create 2019/11/9 22:48
 */
public class test
{
	public static void main(String[] args) throws  IOException
	{
		Socket socket=new Socket("localhost", 11111);
		BufferedOutputStream bos=new BufferedOutputStream(socket.getOutputStream());
		bos.write("Connection established".getBytes());
		bos.close();
	}
}
