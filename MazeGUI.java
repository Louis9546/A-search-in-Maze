/**
 *
 * @author ZE
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;


public class MazeGUI extends JPanel implements Runnable{
    int Size = Astar.getSize();
    int maz[][]=new int[Size][Size]; //matrix used to save  maze.
    int Path[]=new int[3000]; //matrix used to save path.
    int secret=600;// store coordinates and restore them.
    final static int unbockCell=1;
    final static int blockCell=0;
    final static int startCell=2;
    final static int goalCell=3;
    final static int pathCell=4;
    int rows=Size;
    int columns=Size;
    int border=0;
    int sleepTIme=5000;
    int speedSleep=30;
    int blockSize=15;
    int width=-1;
    int height=-1;
    int totalWidth,totalHeight,left,top;
    Color []color;

    //read maze from local file and save it into an int matrix.
    public void readMaze(String path) throws FileNotFoundException, IOException{
        String str= new String();
        BufferedReader br=new BufferedReader(new FileReader(path));

        int b=0;
        while((str=br.readLine())!=null)
        {
            String[]s=str.split(" ");//SIZE==s.length

            for (int j = 0; j < Size; j++)
            {
                maz[b][j]=Integer.parseInt(s[j]);
            }
            b++;
        }
    }

    //redraw maze when update RIght!
    void redrawMaze(Graphics g){
        //draw the entire maze
        int w=totalWidth/columns;//width of each cell
        int h=totalHeight/rows;//height of each cell
        for(int i=0;i<rows;i++){
            for (int j=0; j<columns; j++) {
                if(maz[i][j] < 0)
                    g.setColor(color[blockCell]);
                else
                    g.setColor(color[maz[i][j]]);
                g.fillRect((j*w)+left, (i*h)+top, w, h);
            }
        }
    }

    //read path from local file and save it into an int matrix.
    int a=0;//the number of cell in path(contain start and target)
    public void readPath(String path) throws FileNotFoundException, IOException{
        String str= new String();
        BufferedReader br=new BufferedReader(new FileReader(path));
        while ((str=br.readLine())!=null)
        {
            String[]s=new String[2];
            s=str.split(",");//SIZE==s.length
            Path[a] =Integer.parseInt(s[0])*secret+Integer.parseInt(s[1]);
            a++;
        }
    }

    //draw the start point and target point
    public void endPoint(Graphics g) {
        int w=totalWidth/columns;//width of each cell
        int h=totalHeight/rows;//height of each cell
        int i=(Integer)(Path[0]/secret);//row of start
        int j=(Integer)(Path[0]%secret); //column of start
        int tRow=(Integer)(Path[a-1]/secret);//row of target
        int tCol=(Integer)(Path[a-1]%secret); //column of target

        g.setColor(color[startCell]);
        g.fillRect((j*w)+left, (i*h)+top, w, h);
        g.setColor(color[goalCell]);
        g.fillRect((tCol*w)+left, (tRow*h)+top, w, h);
    }


    public void drawPath(Graphics g){
        int w=totalWidth/columns;//width of each cell
        int h=totalHeight/rows;//height of each cell
        int i;//the row of cell in path
        int j;//the column of cell in path;
        for (int x=1; x<a-1; x++)
        {
            i=(Integer)(Path[x]/secret);
            j=(Integer)(Path[x]%secret);
            g.setColor(color[pathCell]);
            g.fillRect((j*w)+left, (i*h)+top, w, h);
        }
    }


    public MazeGUI(){
        color=new Color[]{
                Color.black,//blocked
                Color.white,//unblocked
                Color.green,//start
                Color.BLUE,//goal
                Color.RED,//Path
                Color.lightGray,//closed
        };
        setBackground(color[blockCell]);
        setPreferredSize(new Dimension(blockSize*columns,blockSize*rows));
        new Thread(this).start();
    }

    //Called before drawing the maze, to set parameters used for drawing.
    void checkSize(){
        if (getWidth()!= width|| getHeight()!= height){
            width=getWidth();
            height=getHeight();
            int w=(width-2*border)/columns;
            int h=(height-2*border)/rows;
            left=(width-w*columns)/2;
            top=(height-h*rows)/2;
            totalWidth=w*columns;
            totalHeight=h*rows;
        }
    }


    synchronized protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        checkSize(); //set parameters used for drawing
        redrawMaze(g);
        drawPath(g);
        endPoint(g);
    }


    @Override
    public void run() {
        try {
            readMaze("maze.txt");
        } catch (IOException ex) {
            Logger.getLogger(MazeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            readPath("path.txt");
        } catch (IOException ex) {
            Logger.getLogger(MazeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        repaint();
    }
}
