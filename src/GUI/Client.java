package GUI;

import Tools.JDBCUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author paulalan
 * @create 2019/11/10 18:07
 */
public class Client
{
	public static void main(String[] args) throws SQLException
	{
		Connection conn;
		PreparedStatement ps;
		ResultSet rs;
		int id = -1;
		String question = "";
		String answer = "";
		int type = -1;
		int answerNum = -1;
		Map<Integer,String> numRelatAnswer=new HashMap<>(){{
			put(0, "A");
			put(1, "B");
			put(2, "C");
			put(3, "D");
		}};


		JFrame client = new JFrame("Client");
		client.setLayout(new BorderLayout());
		client.setVisible(true);
		client.setSize(900, 600);
		client.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout());
		client.add(buttonPanel, BorderLayout.SOUTH);

		JTextArea questionArea = new JTextArea();
		questionArea.setEditable(false);


		conn = JDBCUtil.getConn();
		int randomID = createRandomID();
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

		questionArea.setText(question);
		for (int i = 0; i < answerNum; i++)
		{
			JButton button=new JButton(numRelatAnswer.get(i));
			buttonPanel.add(button);
		}


		JButton refersh=new JButton("Refersh");
		refersh.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				super.mouseClicked(e);
			}
		});

		client.add(refersh,BorderLayout.NORTH);
		client.add(questionArea, BorderLayout.CENTER);
	}

	public static int createRandomID()
	{
		return (int) (Math.random() * 8);
	}


}
