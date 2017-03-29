import java.util.Arrays;

/**
 * Created by Hao on 2/16/17.
 */
public class Heap {
    private int amount;//the amount of cells in the heap
    private State[] heap;

    public Heap(){
        amount = 0;
        heap = new State[2];
        heap[0] = new State();
        heap[1] = new State();
    }

    public void insert(State s){
        if(amount == heap.length - 1)
            doubleSize();

        //Insert the new cell s to the position p
        int p = ++amount;

        while(p > 1 && State.compare_f(s, heap[p/2])) {
            heap[p] = heap[p/2]; //[p/2] is where the root of s is
            p = p/2;
        }
        heap[p] = s;
    }

    //double the size of the heap
    public void doubleSize() {
        State[] previous = heap;
        heap = new State[heap.length * 2];
        for (int i = 0; i < heap.length; i++)
            heap[i] = new State();
        System.arraycopy(previous, 1, heap, 1, amount);
    }

    //delete the top cell
    public State deleteTop() throws RuntimeException{
        if (amount == 0)
            throw new RuntimeException();
        State top = heap[1];
        heap[1] = heap[amount];
        rebuild(1);
        amount--;
        return top;
    }

    //rebuild the tree after deleting the top cell
    public void rebuild(int i){
        State tmp = heap[i];
        int child;

        while(2 * i <= amount){
            child = 2 * i;
            if (child != amount && State.compare_f(heap[child + 1], heap[child]))
                child++;
            if (State.compare_f(heap[child], tmp))
                heap[i] = heap[child];
            else
                break;
            i = child;
        }
        heap[i] = tmp;
    }

    /*
    public static void main(String[] args){
        Heap h = new Heap();
        State[] s = new State[6];
        for (int i = 0; i < 6; i++){
            s[i] = new State();
            s[i].set_g(6-i);
            s[i].set_h(6-i);
            h.insert(s[i]);
        }
        h.printHeap();
    }*/

    //break ties according to g-value, either in favor of smaller g or larger g
    public void comp_root_left(String favor){
        if (amount <= 1)
            return;
        if (heap[1].get_f() == heap[2].get_f()) {
            if (favor == "smaller"){
                if (heap[1].get_g() <= heap[2].get_g())
                    return;
                else
                    switch_root_child(2);
            }
            else if (favor == "larger"){
                if (heap[1].get_g() >= heap[2].get_g())
                    return;
                else
                    switch_root_child(2);
            }
        }
    }

    public void comp_root_right(String favor){
        if (amount <= 2)
            return;
        if (heap[1].get_f() == heap[3].get_f()) {
            if (favor == "smaller"){
                if (heap[1].get_g() <= heap[3].get_g())
                    return;
                else
                    switch_root_child(3);
            }
            else if (favor == "larger"){
                if (heap[1].get_g() >= heap[3].get_g())
                    return;
                else
                    switch_root_child(3);
            }
        }
    }

    public void switch_root_child(int child){
        State tmp = new State();
        tmp = heap[1];
        heap[1] = heap[child];
        heap[child] = tmp;
    }

    public void printHeap(){
        for (int i = 1; i <= amount; i++)
            System.out.println(heap[i].get_h());
    }

    public boolean empty(){
        return amount == 0;
    }

}
