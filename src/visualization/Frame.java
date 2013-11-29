package visualization;

public class Frame {
	private Flower[] flowers;
	private long time;
	private int id;
	
	public Flower[] getFlowers() {
		return flowers;
	}

	public void setFlowers(Flower[] flowers) {
		this.flowers = flowers;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Frame (int id, int time, Flower[] flowers){
		this.flowers = flowers;
		this.time = time;
		this.id = id;
	}
}
