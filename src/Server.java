import Tools.JDBCUtil;
import com.mysql.cj.protocol.Message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	}};

//	/**
//	 * reflection between user name and socket
//	 */
//	private static HashMap<String, Socket> socketsfromUserNames = new HashMap<>();
//	/**
//	 * online user list
//	 */
//	private static ArrayList<String> onlineUsers = new ArrayList<>();

	public static void main(String[] args) throws IOException
	{
		System.out.println("Server start!");

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
		// read message object which comes from client
		private ObjectInputStream ois;
		private BufferedInputStream bis;

		/**
		 * constructor
		 *
		 * @param s socket
		 * @throws IOException
		 */
		public ServerThread(Socket s) throws IOException
		{

			this.s = s;
			//initialize the object input stream
//			ois = new ObjectInputStream(s.getInputStream());
			bis = new BufferedInputStream(s.getInputStream());
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
		}

		public int createRandomID()
		{
			return (int) (Math.random() * 8);
		}

//		/**
//		 * send message object(point to point)
//		 *
//		 * @param message
//		 * @param socket
//		 * @throws IOException
//		 */
//		private void send(Message message, Socket socket) throws IOException
//		{
//			System.out.println("Point to point message: " + message);
//			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//			oos.writeObject(message);
//			System.out.println("Message(P) send successfully!");
//		}

//		/**
//		 * send message object(to All)
//		 *
//		 * @param message
//		 * @param removeOwn
//		 */
//		private void sendToAll(Message message, boolean removeOwn) throws IOException
//		{
//			System.out.println("To all message: " + message);
//			//initialize the object output stream
//			ObjectOutputStream oos;
//			if (removeOwn)
//			{
//				//acquire Username-Socket of hashmap to traverse to send
//				for (HashMap.Entry<String, Socket> entry : socketsfromUserNames.entrySet())
//				{
//					//send to all except oneself
//					if (!entry.getKey().equals(message.getMap().get("From")))
//					{
//						oos = new ObjectOutputStream(entry.getValue().getOutputStream());
//						oos.writeObject(message);
//					}
//				}
//			} else
//			{
//				//send to everyone
//				for (Socket socket : socketsfromUserNames.values())
//				{
//					oos = new ObjectOutputStream(socket.getOutputStream());
//					oos.writeObject(message);
//				}
//			}
//			System.out.println("Message(A) send successfully!");
//		}
//
//		/**
//		 * check connect and send online notification
//		 *
//		 * @param message
//		 * @throws IOException
//		 */
//		private void checkConnect(Message message) throws IOException
//		{
//			String username = message.getFrom();
//			System.out.println(username + "is logining...");
//			//check user name from existed username-socket list
//			if (!socketsfromUserNames.containsKey(username))
//			{
//				socketsfromUserNames.put(username, s);
//				onlineUsers.add(message.getFrom());
//
//				System.out.println(username + "login successfully!");
//				//feedback message(success)
//				Message sResult = new Message();
//				sResult.setType("SUCCESS");
//				sResult.setContent(username + " connect successfully!");
//
//				send(sResult, s);
//				//update list
//				sendOnlineUserList(true);
//				//send online notification
//				sendNotification(username + " is online!");
//			} else
//			{
//				System.out.println(username + " login failed!");
//				//feedback message(fail)
//				Message fResult = new Message();
//				fResult.setType("FAIL");
//				fResult.setContent(fResult.getFrom() + " is existed, connect failed!");
//				//send
//				send(fResult, s);
//			}
//		}
//
//		/**
//		 * close the connect and send offline notification
//		 *
//		 * @param message
//		 * @throws IOException
//		 */
//		private void closeConnect(Message message) throws IOException
//		{
//			String userName = message.getFrom();
//			System.out.println(userName + " ready to login out!");
//			Socket socket = socketsfromUserNames.get(userName);
//			if (socket != null)
//			{
//				socketsfromUserNames.remove(userName);
//			}
//			System.out.println(userName + " login out successful! Ready to update online user list!");
//			//update list
//			onlineUsers.removeIf(temp -> temp.equals(userName));
//			sendOnlineUserList(true);
//			//send offline notification
//			sendNotification(userName + " is offline!");
//		}
//
//		/**
//		 * send online user list
//		 *
//		 * @param isAllUsers
//		 * @throws IOException
//		 */
//		private void sendOnlineUserList(boolean isAllUsers) throws IOException
//		{
//			System.out.println("Ready to update online user list!");
//			//feedback message
//			Message uResult = new Message();
//			uResult.setType("USERLIST");
//			uResult.setUserlist(onlineUsers);
//			if (isAllUsers)
//			{
//				//send to all
//				sendToAll(uResult, false);
//			} else
//			{
//				send(uResult, s);
//			}
//			System.out.println("Online user list update successfully!");
//		}
//
//		/**
//		 * send notification to users
//		 *
//		 * @param notice notification
//		 * @throws IOException
//		 */
//		private void sendNotification(String notice) throws IOException
//		{
//			System.out.println("Ready to send notification message!");
//			Message message = new Message();
//			message.setType("NOTIFICATION");
//			message.setContent(notice);
//			sendToAll(message, false);
//		}
//
//		/**
//		 * send message from client to client / All
//		 *
//		 * @param message message from client
//		 * @throws IOException
//		 */
//		private void sendMSG(Message message) throws IOException
//		{
//			String userName = message.getFrom();
//			String toUser = message.getTo();
//			//check receiver
//			if (toUser.equals(Message.ALL))
//			{
//				System.out.println(userName + " is sending to All!");
//				// unnecessary to send to oneself
//				sendToAll(message, true);
//			} else
//			{
//				System.out.println(userName + " is sending to user!");
//				send(message, socketsfromUserNames.get(toUser));
//			}
//		}
//	}
	}
}
