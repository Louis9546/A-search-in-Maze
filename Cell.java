/**
 * Created by Hao on 2/14/17.
 */
public class Cell {
    protected boolean visit;
    protected boolean block; //true -> blocked; false -> unblocked;

    public Cell(){
        this.visit = false;
    }

    public void setVisit(boolean visit){
        this.visit = visit;
    }

    public void setBlock(boolean block){
        this.block = block;
    }

    public boolean getVisit(){
        return visit;
    }

    public boolean getBlock(){
        return block;
    }
}
