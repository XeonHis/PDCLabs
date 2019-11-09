package Tools;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author paulalan
 * @create 2019/8/27 14:58
 */
public class JDBCUtil
{
	static String driverClass = null;
	static String url = null;
	static String name = null;
	static String password = null;

	static
	{
		try
		{
			//1. 创建属性配置对象
			Properties properties = new Properties();
			//使用类加载器，去读取src底下的资源文件
			InputStream is= JDBCUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
			properties.load(is);
			driverClass=properties.getProperty("driverClass");
			url=properties.getProperty("url");
			name=properties.getProperty("name");
			password=properties.getProperty("password");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Connection getConn()
	{
		Connection conn = null;
		try
		{
			//1. 注册驱动
			Class.forName(driverClass);

			//2. 建立连接
			conn = DriverManager.getConnection(
					url, name, password);
			System.out.println("Connection established!");
		} catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		return conn;
	}

	public static void release(Connection conn, Statement st, ResultSet rs)
	{
		closeRs(rs);
		closeSt(st);
		closeConn(conn);
	}

	public static void release(Connection conn, Statement st)
	{
		closeSt(st);
		closeConn(conn);
	}

	private static void closeRs(ResultSet rs)
	{
		try
		{
			if (rs != null)
			{
				rs.close();
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			rs = null;
		}
	}

	private static void closeSt(Statement st)
	{
		try
		{
			if (st != null)
			{
				st.close();
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			st = null;
		}
	}

	private static void closeConn(Connection conn)
	{
		try
		{
			if (conn != null)
			{
				conn.close();
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			conn = null;
		}
	}
}
