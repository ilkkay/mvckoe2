package translateit2.restapi;

import java.util.List;

import translateit2.persistence.dto.UnitDto;

public class Units {
    private long pageCount;
    private List<UnitDto> units;

    Statistics statistics;

    public long getPageCount() {
        return pageCount;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public List<UnitDto> getUnits() {
        return units;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public void setUnits(List<UnitDto> units) {
        this.units = units;
    }
}
