/**
 * Created by Hao on 2/14/17.
 */
import java.util.*;
import java.io.*;

public class Maze {

    private int SIZE;

    private Cell[][] maze;
    
    
    public Maze(int size){
        SIZE = size;
        maze = new Cell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++)
                maze[i][j] = new Cell();
        }
    }
    
    
    
//    public void setInt( int ma[][]){
//        ma=new int[SIZE][SIZE];
//        for(int i=0;i<SIZE;i++){
//                for(int j=0;j<SIZE;j++){
//                    if(maze[i][j].getBlock()){
//                        
//                        ma[i][j]=0;
//                        System.out.print(ma[i][j]);//test
//                    } 
//                    else {
//                        
//                        ma[i][j]=1;
//                        System.out.print(ma[i][j]);//test
//                    }
//                    }
//               System.out.println();
//         }
//    }
    

    public void generateMaze(){
        int visit = 0; //initialize visit times = 0;
        Stack s = new Stack();


        while(visit < SIZE * SIZE){
            //randomly choose the start cells
            int i = new Random().nextInt(SIZE);
            int j = new Random().nextInt(SIZE);

            //find the start point from unvisited cells
            if (maze[i][j].getVisit())
                continue;

            s.push(get_index(i, j, SIZE));
            while(!s.empty()) {
                i = (Integer) s.peek() / SIZE;
                j = (Integer) s.peek() % SIZE;
                s.pop();

                if (!maze[i][j].getVisit()) {
                    maze[i][j].setVisit(true);
                    visit++;
                    double probability = new Random().nextInt(100);
                    probability = probability / 100;
                    if (probability < 0.3)
                        maze[i][j].setBlock(true);
                    else
                        maze[i][j].setBlock(false);

                    //upwards
                    if (i > 0 && !maze[i - 1][j].getVisit()){
                        s.push(get_index(i - 1, j, SIZE));
                        continue;
                    }

                    //downwards
                    if (i < SIZE - 1 && !maze[i + 1][j].getVisit()) {
                        s.push(get_index(i + 1, j, SIZE));
                        continue;
                    }

                    //leftwards
                    if (j > 0 && !maze[i][j - 1].getVisit()) {
                        s.push(get_index(i, j - 1, SIZE));
                        continue;
                    }

                    //rightwards
                    if (j < SIZE - 1 && !maze[i][j + 1].getVisit())
                        s.push(get_index(i, j + 1, SIZE));
                        continue;
                }
            }
        }
    }

    //change the 2 dimensional array index into 1 dimensional array
    public static int get_index(int i, int j, int SIZE){
        return i * SIZE + j;
    }

    /*
    public static void main(String[] args){
        Maze m = new Maze();
        m.generateMaze();
        m.store();

        m.read();

        m.print();

    }*/

    //store the maze to a text file
    /*
    public void store() {
        try {
            File file = new File("maze.txt");
            FileWriter fileWriter = new FileWriter(file);
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (this.maze[i][j].getBlock())
                        fileWriter.write("0 ");
                    else
                        fileWriter.write("1 ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void read(String path) throws FileNotFoundException, IOException{
        String str= new String();
        BufferedReader br=new BufferedReader(new FileReader(path));
       int[][] Maz=new int[SIZE][SIZE];//use matrix to save information from local file(Maze).
        int a=0;


        while((str=br.readLine())!=null)
        {
            String[]s=str.split(" ");//SIZE==s.length

                for (int j = 0;j < SIZE; j++){
                    
               Maz[a][j]=Integer.parseInt(s[j]);
                    if (Integer.parseInt(s[j]) == 1)
                        this.maze[a][j].setBlock(false);
                    else
                        this.maze[a][j].setBlock(true);
                }
                a++;

            
            //for(int n=0;n<SIZE;n++){  
        }
    }

    public void store() throws IOException{
        File file = new File("maze.txt");  //the file used to store information.
        FileWriter out = new FileWriter(file);
        for(int i=0;i<SIZE;i++){
            for(int j=0;j<SIZE;j++){
                if (this.maze[i][j].getBlock())
                    out.write("0 ");
                else
                    out.write("1 ");
            }
            out.write("\r\n");
        }
        out.close();

    }


    //read maze from a text file
    /*
    public void read() {
        try{
            File file = new File("maze.txt");
            FileReader fileReader = new FileReader(file);
            char[] data = new char[SIZE * SIZE];
            fileReader.read(data);
            int k = 0;
            for(int i = 0; i < SIZE; i++){
                for (int j = 0; j < SIZE; j++){
                    if (data[k++] == '1')
                        this.maze[i][j].setBlock(false);
                    else
                        this.maze[i][j].setBlock(true);
                }

            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    */

    public void print(){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++) {
                if (this.maze[i][j].getBlock())
                    System.out.print(0 + " ");
                else
                    System.out.print(1 + " ");
            }
            System.out.println();
        }
    }

    public boolean block(int i, int j){
        return maze[i][j].getBlock();
    }
}
