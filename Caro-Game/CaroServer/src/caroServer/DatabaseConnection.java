package caroServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnection {
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public DatabaseConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/tp1", "root", "hihi");
			pstmt = con.prepareStatement("select * from tp1.players where username=? and password=?");
			con.setAutoCommit(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public PreparedStatement getPstmt() {
		return pstmt;
	}

	public void setPstmt(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public Boolean checkLogin(String uname, String pwd) {
		try {

			pstmt.setString(1, uname);
			pstmt.setString(2, pwd);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error while validating" + e);
			return false;
		}
	}
}
