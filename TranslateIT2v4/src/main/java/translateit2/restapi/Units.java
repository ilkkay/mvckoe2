package translateit2.restapi;

import java.util.List;

import translateit2.persistence.dto.UnitDto;

public class Units {
	private List<UnitDto> units;
	Statistics statistics;

	private long pageCount;
	
	public List<UnitDto> getUnits() {
		return units;
	}

	public void setUnits(List<UnitDto> units) {
		this.units = units;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}
}
