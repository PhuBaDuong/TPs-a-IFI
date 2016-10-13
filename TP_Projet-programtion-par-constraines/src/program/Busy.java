package program;

public class Busy {
	private int day;
	private int slot;

	public Busy(int day, int slot) {
		this.day = day;
		this.slot = slot;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}
}
