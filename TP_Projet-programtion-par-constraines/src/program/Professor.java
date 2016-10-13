package program;

import java.util.ArrayList;
import java.util.List;

public class Professor {
	private int id;
	private String name;
	private List<Busy> busyList;

	public Professor() {
		this.id = 0;
		this.name = "";
		this.busyList = new ArrayList<Busy>();
	}
	
	public Professor(int id, String name, List<Busy> busyList) {
		super();
		this.id = id;
		this.name = name;
		this.busyList = busyList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Busy> getBusyList() {
		return busyList;
	}

	public void setBusyList(List<Busy> busyList) {
		this.busyList = busyList;
	}
}
