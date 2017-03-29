/**
 * Created by Hao on 2/16/17.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.swing.JFrame;

public class Astar {

    private static int SIZE = 101;
    private LinkedList<Integer> closedlist; //store the one dimensional index of the cell
    private Heap openlist;
    private Maze maze;
    private State[][] grid; //grid is the maze that the agent currently knows
    private State origin;
    private State dest; //destination

    private int i_origin;
    private int j_origin;
    private int i_dest;
    private int j_dest;

    public static int getSize(){
        return SIZE;
    }

    //initialize list and maze, grid.
    public Astar(){
        closedlist = new LinkedList<Integer>();
        openlist = new Heap();
        maze = new Maze(SIZE);
        grid = new State[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++)
                grid[i][j] = new State();
        }

        origin = new State();
        dest = new State();
    }

    //set each state when encountered
    public void set_state(int i, int j){
        if (!maze.block(i, j)) {
            grid[i][j].set_g(grid[i][j].getParent().get_g() + 1);
            grid[i][j].set_h(dest);
            grid[i][j].set_i(i);
            grid[i][j].set_j(j);
            grid[i][j].setVisit(true);
            openlist.insert(grid[i][j]);
        }
        
        else {
            grid[i][j].setBlock(true);
            grid[i][j].setVisit(true);
        }

    }

    public void initializeStart(int i, int j){
        origin = new State();
        origin = grid[i][j];
        origin.setBlock(false);

        origin.set_i(i);
        origin.set_j(j);
        origin.setVisit(true);
        origin.set_g(0);
        closedlist.add(Maze.get_index(origin.get_i(), origin.get_j(), SIZE));
    }

    public void initializeDest(int i, int j){
        dest = new State();
        dest = grid[i][j];
        dest.setBlock(false);
        dest.set_i(i);
        dest.set_j(j);

    }

    public void initialize(){
        //initialize the maze and the grid
        maze.generateMaze();
        try {
            maze.store();
            maze.read("maze.txt");
        } catch (Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++)
                grid[i][j] = new State();
        }

        //randomly choose the starting point and the destination
        while (true) {
            int i = new Random().nextInt(SIZE);
            int j = new Random().nextInt(SIZE);
            int m = new Random().nextInt(SIZE);
            int n = new Random().nextInt(SIZE);
            if (!maze.block(i, j) && !maze.block(m, n)) {

                //initialize the starting point
                initializeStart(i, j);

                //initialize the destination
                initializeDest(m, n);

                break;
            }
        }

        i_origin = origin.get_i();
        j_origin = origin.get_j();
        i_dest = dest.get_i();
        j_dest = dest.get_j();

    }

    //used to restore the grid world for next use but without changing the start point and destination
    public void restore(String choice){
        closedlist = new LinkedList<Integer>();
        openlist = new Heap();
        grid = new State[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++) {
                    grid[i][j] = new State();
            }
        }

        if (choice == "forward") {
            initializeStart(i_origin, j_origin);
            initializeDest(i_dest, j_dest);
        }
        else if (choice == "backward"){
            initializeStart(i_dest, j_dest);
            initializeDest(i_origin, j_origin);
        }
    }
    
    public void Forward(String choice) throws IOException {


//        origin.setVisit(true);
//        origin.set_g(0);
//        closedlist.add(Maze.get_index(origin.get_i(), origin.get_j(), SIZE));



        //search begins
        do {
            int i = origin.get_i();
            int j = origin.get_j();

            //check if the destination is reached
            if (i == dest.get_i() && j == dest.get_j()) {
                System.out.println("Mission complete. The destination is reached");
                

                //create the path
                ArrayList<State> path = new ArrayList<State>();
                State node = dest;
                while(node != null){
                    path.add(node);
                    node = node.getParent();
                }

                //Output the path and save the path into local file :"path.txt".
                File file = new File("path.txt");  //the file used to store information.
                FileWriter out = new FileWriter(file);
                for (int n = path.size() - 1; n >= 0; n--){
                    System.out.println("(" + path.get(n).get_i() + ", " + path.get(n).get_j() + ")");
                    out.write(path.get(n).get_i() + "," + path.get(n).get_j());
                out.write("\r\n");
              }
                System.out.println();
                 out.close();
                 return;
            }

            //check the upper cell
            if (i > 0 && !grid[i - 1][j].getVisit()) {
                grid[i - 1][j].setParent(grid[i][j]);
                set_state(i - 1, j);
            }

            //check the lower cell
            if (i < SIZE - 1 && !grid[i + 1][j].getVisit()) {
                grid[i + 1][j].setParent(grid[i][j]);
                set_state(i + 1, j);
            }

            //check the left cell
            if (j > 0 && !grid[i][j - 1].getVisit()) {
                grid[i][j - 1].setParent(grid[i][j]);
                set_state(i, j - 1);
            }

            //check the right cell
            if (j < SIZE - 1 && !grid[i][j + 1].getVisit()) {
                grid[i][j + 1].setParent(grid[i][j]);
                set_state(i, j + 1);
            }

            //make sure the top element of the openlist has the smallest f

            openlist.comp_root_left(choice);
            openlist.comp_root_right(choice);

            origin = openlist.deleteTop();
            closedlist.add(Maze.get_index(origin.get_i(), origin.get_j(), SIZE));

        } while (!openlist.empty());

        System.out.println("The destination can not be reached");
        System.out.println();
    }

    public void Backward(String choice) throws IOException {

        restore("backward");
        System.out.println("The starting point is " + "(" + origin.get_i() + ", " + origin.get_j() + ")");
        System.out.println("The destination is " + "(" + dest.get_i() + ", " + dest.get_j() + ")");
        Forward(choice);
    }

    public static void main(String[] args) throws IOException{
        Runtime r=Runtime.getRuntime(); 
        double beginMemory=r.freeMemory();
        Scanner scan = new Scanner(System.in); 
        Scanner scan2 = new Scanner(System.in);
        Astar a = new Astar();
        a.initialize();
        int selection = 1;//enter the loop
        while (selection!=0){
            System.out.println("'1' : Repeated Forward A*");
            System.out.println("'2' : Repeated Backward A*");
            System.out.println("'0' : Exit");
            System.out.println("Please type your choice");
            selection = scan.nextInt();
            if(selection==0){
                break;
            }
            System.out.println("please input 'smaller' or 'larger' to decide how to break ties");
            String choice = scan2.nextLine();
            long startTime = System.currentTimeMillis();          
            switch (selection){
                case 1:
                    a.restore("forward");
                    System.out.println("The starting point is " + "(" + a.origin.get_i() + ", " + a.origin.get_j() + ")");
                    System.out.println("The destination is " + "(" + a.dest.get_i() + ", " + a.dest.get_j() + ")");
                    a.Forward(choice);  
                    a.gui("Forward" + " in favor of " + choice + " g");
                    break;
                    
                case 2:
                    a.Backward(choice);    
                    a.gui("Backward" + " in favor of " + choice + " g");
                    break;
                
            }   
            long endTime = System.currentTimeMillis();
            System.out.println("The time being used is " + (endTime- startTime) + "ms");
            double endMenory=r.totalMemory();
            System.out.println("The memory cost is: "+(endMenory-beginMemory)/(1024.0*1024.0)+" mb");
    }    
    }
    public void gui(String title){
        JFrame window=new JFrame(title);
        window.setContentPane(new MazeGUI());
        window.pack();
        window.setLocation(100, 80);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}
