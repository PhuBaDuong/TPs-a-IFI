package server;

public class Account {
	private String name;
	private String pwd;

	public Account() {
		super();
	}

	public Account(String name, String pwd) {
		super();
		this.name = name;
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public boolean verifyAcc(Account account) {
		if (this.getName().equals(account.getName()) && this.getPwd().equals(account.getPwd())) {
			return true;
		}
		return false;
	}
}
