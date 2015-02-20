package pawn.model.dto;

/**
 * Created by Mikalai_Dzerachyts on 2/20/2015.
 */
public class MoveDto {
    private int from;
    private int to;

    public MoveDto() {
    }

    public MoveDto(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
