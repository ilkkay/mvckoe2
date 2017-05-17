package translateit2.restapi;

public class Statistics {
	private long reviewed;
	
	private long translated;
	
	private long total;

	public long getReviewed() {
		return reviewed;
	}

	public void setReviewed(long reviewed) {
		this.reviewed = reviewed;
	}

	public long getTranslated() {
		return translated;
	}

	public void setTranslated(long translated) {
		this.translated = translated;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
