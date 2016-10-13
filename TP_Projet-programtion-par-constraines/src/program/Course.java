package program;

import java.util.List;

public class Course {

	private int id;
	private String name;
	private int instructorId;
	private int numberOfSlots;
	private List<ClassName> classNames;

	public Course(int id, String name, int instructorId,
			int numberOfSlots, List<ClassName> classNames) {
		super();
		this.id = id;
		this.name = name;
		this.instructorId = instructorId;
		this.numberOfSlots = numberOfSlots;
		this.classNames = classNames;
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

	public int getInstructor() {
		return instructorId;
	}

	public void setInstructor(int instructorId) {
		this.instructorId = instructorId;
	}

	public int getNumberOfSlots() {
		return numberOfSlots;
	}

	public void setNumberOfSlots(int numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
	}

	public List<ClassName> getClassNames() {
		return classNames;
	}

	public void setClasses(List<ClassName> classNames) {
		this.classNames = classNames;
	}

}
