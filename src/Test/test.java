package Test;

import Tools.JDBCUtil;
import Tools.Question;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.*;

/**
 * @author paulalan
 * @create 2019/11/9 22:48
 */
public class test
{
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		Socket socket = new Socket("localhost", 11111);
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		Question question = (Question) ois.readObject();
		System.out.println("id = " + question.getId() + "\nquestion = " + question.getQuestion() +
				"\nanswer = " + question.getAnswer() + "\ntype = " + question.getType() +
				"\nanswerNum = " + question.getAnswerNum());
	}
}
