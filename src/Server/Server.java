package Server;

import Tools.JDBCUtil;
import Tools.Question;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author paulalan
 * @create 2019/11/12 22:00
 */
public class Server
{
	/**
	 * server port
	 */
	private static final int SERVER_PORT = 11111;

	private static Connection conn;
	private static PreparedStatement ps;
	private static ResultSet rs;

	private static int id = -1;
	private static int randomID = -1;
	private static String question = "";
	private static String answer = "";
	private static int type = -1;
	private static int answerNum = -1;
	private static Map<Integer, String> namRelateAnswer = new HashMap<>()
	{{
		put(0, "A");
		put(1, "B");
		put(2, "C");
		put(3, "D");
		put(4, "E");
		put(5, "F");
		put(6, "G");
	}};


	public static void main(String[] args) throws IOException
	{
		System.out.println("Server.Server start!");

		ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
		try
		{
			while (true)
			{
				Socket s = serverSocket.accept();
				new Thread(new ServerThread(s)).start();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * socket thread which is used by server
	 */
	public static class ServerThread implements Runnable
	{
		private Socket s;
		private ObjectOutputStream oos;
		Question questionDetail = new Question();


		/**
		 * constructor
		 *
		 * @param s socket
		 * @throws IOException
		 */
		public ServerThread(Socket s) throws IOException
		{

			this.s = s;
			oos = new ObjectOutputStream(s.getOutputStream());
		}

		@Override
		public void run()
		{
			try
			{
				if (!s.isClosed())
				{
					System.out.println(s + " port has connected to the server!");
				}

				while (s.isConnected())
				{
					generateQuestion();
					oos.writeObject(questionDetail);
					oos.close();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		private void generateQuestion() throws SQLException
		{
			randomID = createRandomID();
			conn = JDBCUtil.getConn();
			String detail = "select * from myTest where id=?";
			ps = conn.prepareStatement(detail);
			ps.setString(1, String.valueOf(randomID));
			rs = ps.executeQuery();
			while (rs.next())
			{
				id = rs.getInt("id");
				question = rs.getString("question");
				answer = rs.getString("answer");
				type = rs.getInt("type");
				answerNum = rs.getInt("answerNum");
			}
			JDBCUtil.release(conn, ps, rs);
			System.out.println("id = " + id + "\nquestion = " + question + "\nanswer = " + answer +
					"\ntype = " + type + "\nanswerNum = " + answerNum);
			questionDetail.setItems(id, question, answer, type, answerNum);
		}

		public int createRandomID()
		{
			return (int) (Math.random() * 8);
		}

	}
}
