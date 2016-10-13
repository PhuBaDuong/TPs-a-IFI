package program;

public class ClassName {
	private String name;

	public ClassName(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(ClassName a) {
		return this.name.equals(a.getName());
	}
}
