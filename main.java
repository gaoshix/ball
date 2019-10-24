
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;


class GamePanel extends JPanel implements KeyListener
{
 
 char[][] map; 
 char[][] box;
 int X,Y;  //Map size
 Point player,p;
 int Num=0;  
 Point[] boxs; 

 Point[] dir;
 
 int size;
 Image floor;
 Image wall;
 Image player1;
 Image happy;
 Image ball;
 Image angry;
 
 //method 
 public GamePanel(char b[][],int w,int h,int size)throws Exception
 {

  X = b.length;
  Y = b[0].length;
  Num = 0;
  
  map =   new char[X][Y];
  box =   new char[X][Y];
  player= new Point();
  p = new Point();
  boxs= new Point[X*Y];
  
  dir = new Point[4];
  dir[0]= new Point(0,-1); 
  dir[1]= new Point(-1,0); 
  dir[2]= new Point(0, 1); 
  dir[3]= new Point(1, 0); 
  
  File parent = new File("../Gao/images");
  
  floor = ImageIO.read(new File(parent,"floor.gif"));   
  wall  = ImageIO.read(new File(parent,"wall.png"));
  player1= ImageIO.read(new File(parent,"player.png"));
  angry  = ImageIO.read(new File(parent,"angry.png"));
  ball  = ImageIO.read(new File(parent,"ball.png"));
  happy  = ImageIO.read(new File(parent,"happy.png"));
  
  this.size=size;
  
  //set Map
  for(int i=0;i<X;i++){ 
   for(int j=0;j<Y;j++){
    switch(b[i][j]){
     case 'P': //Player
      map[i][j]='.';
      p.setLocation(i, j);
      break;
     case 'X': 
      map[i][j]='X';
      break;
     case 'B': 
      map[i][j]='.';
      boxs[Num++]=new Point(i, j);
      break;
     case '*':
      map[i][j]='*';
      break;
     case '.': 
      map[i][j]='.';
      break;
     default :
      map[i][j]='.';
      break;
    }
   }
  }
  
  reset(); 
  Thread t = new DoMusic(); 
  t.start(); 

 }
 
 public void reset(){
  player.setLocation(p);
  for(int i=0;i<X;i++)
   for(int j=0;j<Y;j++)
    box[i][j]=' ';
  for(int i=0;i<Num;i++)
   box[boxs[i].x][boxs[i].y]='B';
  
  repaint();
 }
 
 public void undo(){
  
 }
 
 public boolean judge(){
  for(int i=0;i<X;i++)
   for(int j=0;j<Y;j++)
    if(box[i][j]=='B'&&map[i][j]!='X')
     return false;
  return true;
 }
 
 public void paint(Graphics g){

  for(int i=0;i<X;i++){
   for(int j=0;j<Y;j++){
    switch(map[i][j]){
     case '.':
      g.drawImage(floor, j*size, i*size, null);
      break;
     case '*':
      g.drawImage(wall, j*size, i*size, null);
      break;
     case 'X':
      g.drawImage(floor, j*size, i*size, null);
      break;
    }
    if(box[i][j]=='B'&&map[i][j]=='X') g.drawImage(happy, j*size, i*size, null);
    else if(box[i][j]=='B') g.drawImage(ball, j*size, i*size, null);
    else if(map[i][j]=='X') g.drawImage(angry, j*size, i*size, null);
   }
  }

  g.drawImage(player1, player.y*size, player.x*size, null);
  
  g.drawRect(0, 0, Y*size, X*size);
  
  this.requestFocusInWindow();
 }
 
 public void keyPressed(KeyEvent e){
  int x=player.x;
  int y=player.y;
  int k,x0,y0;
  
  k=e.getKeyCode()-37;
  
  if(k<0||k>=4) return;
  
  x+=dir[k].x;
  y+=dir[k].y;

  if(x<0||x>=X||y<0||y>=Y) 
	  return;
  if(map[x][y]=='*') 
	  return;
  
  if(box[x][y]!='B'&&(map[x][y]=='.'||map[x][y]=='X')){
   player.x=x;
   player.y=y;
  }
  if(box[x][y]=='B'){
   x0=x+dir[k].x;
   y0=y+dir[k].y;
   if(x0<0||y0<0||x0>=X||y0>=Y) 
	   return;
   if(map[x0][y0]!='*'&&box[x0][y0]!='B'){
    box[x][y]=' ';
    box[x0][y0]='B';
    player.x=x;
    player.y=y;
   }
  }
  
  repaint();
  
  if(judge()){
            JOptionPane.showMessageDialog(null,"恭喜你贏了!!");
   reset();
  }
 }
 
 public void keyReleased(KeyEvent e){}
 public void keyTyped(KeyEvent e){}
 
}

public class t1 extends JFrame 
{

 JPanel top;
 GamePanel center;
 JPanel bottom;
 
 JButton breset; 
 JButton bundo; 
 JButton text;
 
 int width,height,size;
 int X1,Y2;
 

 public  t1(char[][] b)throws Exception{
  super("Push The Ball ");

  //set 視窗
  size=34;
  X1=b.length;
  Y2=b[0].length;
  width=size*Y2+16;
  height=size*X1+110;
  
  top     = new JPanel();
  center  = new GamePanel(b,width,height-100,size); 
  bottom  = new JPanel();
  breset = new JButton("重新鍵");
  bundo = new JButton("Undo");
  text =new JButton("規則");
  
  
  
  center.addKeyListener(center);
  text.addActionListener(new EvetListener());
  breset.addActionListener(new ResetListener());
  bundo.addActionListener(new UndoListener());
  
  
  bottom.add(text);
  bottom.add(breset);
  
  add(top,"North");
  add(center,"Center");
  add(bottom,"South");
  
  setSize(width,height);
  setVisible(true);
 }

 class ResetListener implements ActionListener{
  public void actionPerformed(ActionEvent e){
   center.reset();
  }
 }
 class UndoListener implements ActionListener{
  public void actionPerformed(ActionEvent e){
   center.undo();
  }
 }
 class EvetListener implements ActionListener{
	  public void actionPerformed(ActionEvent e){
		  String title = "遊戲說明";
		  String msg = "以鍵盤操控將球分配給每個臭臉，得球者會轉為笑臉，集滿全部笑臉就算勝利!!";
		  		       	
		  int type = JOptionPane.INFORMATION_MESSAGE;
		  JOptionPane.showMessageDialog(null,msg,title,type);
		  center.reset();
	  }
	 }
  

 public static void main(String[] args)throws Exception{
  char[][] b=new char[22][20];
  
  b[0]="******************P*".toCharArray();
  b[1]="**.....** *..***...*".toCharArray();
  b[2]="**X **.B.B. .X.....*".toCharArray();
  b[3]="*....*....*.....*..*".toCharArray();
  b[4]="*..****...*.B****..*".toCharArray();
  b[5]="*......X...B....*..*".toCharArray();
  b[6]="*..**.........*.*X**".toCharArray();
  b[7]="****.B.*..**..*.*..*".toCharArray();
  b[8]="*.X....*...........*".toCharArray();
  b[9]="*....****..*****..**".toCharArray();
  b[10]="*********  *********".toCharArray();
  b[11]="*********  *********".toCharArray();
  b[12]="***..**....**..*****".toCharArray();
  b[13]="*.X..X...*.**....B.*".toCharArray();
  b[14]="*....B...*.*....*B.*".toCharArray();
  b[15]="**B..*...*.X..X**..*".toCharArray();
  b[16]="**.***.....**..**..*".toCharArray();
  b[17]="*...*..***.*****...*".toCharArray();
  b[18]="*.X.*....*.....B...*".toCharArray();
  b[19]="**....B..*...*..**X*".toCharArray();
  b[20]="**.**..*.....*.....*".toCharArray();
  b[21]="********************".toCharArray();
  
  t1 p=new  t1(b);
  p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }
}
