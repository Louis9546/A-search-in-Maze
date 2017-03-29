/**
 * Created by Hao on 2/15/17.
 */
public class State extends Cell{
    public State(){
        this.block = false;
        this.visit = false;
    }

    private int i; //horizontal index
    private int j; //vertical index
    private int g = 1000; //real distance to the starting point, initially set a large number for comparision
    private int h = 1000; //Manhattan distance to the destination, initially set a large number for comparision
    private State parent;

    public void set_h(State destination){
        this.h = Math.abs(this.i - destination.i) + Math.abs(this.j - destination.j);
    }

    public void set_h(int h){
        this.h = h;
    }

    public int get_h(){
        return this.h;
    }

    public void set_g(int g){
        this.g = g;
    }

    public int get_g(){
        return this.g;
    }

    public int get_f(){
        return this.g + this.h;
    }

    public void set_i(int i){
        this.i = i;
    }

    public int get_i(){
        return this.i;
    }

    public void set_j(int j){
        this.j = j;
    }

    public int get_j(){
        return this.j;
    }

    public void setParent(State s){
        this.parent = s;
    }

    public State getParent(){
        return this.parent;
    }


    public static boolean compare_f(State a, State b){
        return (a.g + a.h) < (b.g + b.h);
    }

    public static boolean compare_g(State a, State b){
        return a.g < b.g;
    }

    public boolean visit(){
        return this.visit;
    }




}
