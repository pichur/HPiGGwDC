package edu.put.pw.hpiggwdc;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Position {

    private Integer id;

    private Integer pos;

    public Position() {
    }
    
    public Position(Integer id, boolean initialized) {
        this.id  = initialized ? id : null;;
        this.pos = id;
    }

    @PlanningVariable(valueRangeProviderRefs = "range")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Vertex{" + "id=" + id + ", pos=" + pos + '}';
    }
}
