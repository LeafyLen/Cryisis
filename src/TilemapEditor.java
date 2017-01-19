import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import javax.lang.model.type.ArrayType;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import java.io.File;
import org.newdawn.slick.opengl.ImageIOImageData;
import java.io.FileFilter;

import javax.imageio.ImageIO;
import javax.naming.NameNotFoundException;
import javax.swing.*;

//import static javax.swing.plaf.metal.MetalBumps.createBuffer;

/**
 *
 *
 * The LibTest class is the main class.
 *
 * Within this class sprites are drawn, deleted, and updated.
 * Basically contains everything the game has to offer.
 * @author Andrey Grebenik (Kuro)
 * @version 1.0
 * @since 2016-12-3
 * */

public class TilemapEditor {
    public static String tool = "pen";
    public static boolean isFullScreen=false;
    //screen width and height controlling variables.
    public static final int W = 1000;
    public static final int H = 740;

    public static GLtile curSel;
    public static GLtile[][] grid = new GLtile[640/32][800/32];
    public static GLtile[][] grid_e = new GLtile[640/32][800/32];
    public static ArrayList<GLtile> tiles = new ArrayList<>();
    public static ArrayList<GLtile> enemies = new ArrayList<>();
    public static ArrayList<String> names = new ArrayList<>();

    //currently variables to save player position to be removed later.
    public static int x = 200;
    public static int y = 100;

    public static GLbutton tp;
    public static GLbutton bt;

    //A stopwatch which holds the time since the game started
    public static final Time gameTime=new Time();

    //images contains all the GLimages currently on screen.
    public static ArrayList<GLimage> images = new ArrayList<>();
    // buttons contains all GLbuttons on the screen.
    public static ArrayList<GLbutton> buttons = new ArrayList<>();
    // text contains all GLtexts on the screen.
    public static ArrayList<GLtext> text = new ArrayList<>();
    // currentTexture is an index of where to add the next GLimage.
    public static int currentTexture = 0;
    // currentTexture is an index of where to add the next GLbutton.
    public static int currentButton = 0;
    // currentTexture is an index of where to add the next GLtext.
    public static int currentText = 0;

    public static int currentUnit = 0;

    public static String curUnit = "";

    public static long lastUpdateTime=0;

    public static String name;

    public static double dt;
    /**
     * start() initializes the GL and then renders, updates, and takes input.
     * Usually this is where you would put anything you would want to happen every tick.
     * @throws IOException
     */
    public static void start() throws IOException , AudioControllerException{
        Tools.p("Level num %area%-%level% example 1-5");
        Tools.p("if you are going to load a level enter the name of the level to load.");
        Tools.p("if you are creating a new level enter the name for the new level.");
        name = Tools.i("enter level num: ");

        initGL(W,H);
        init();

        while (true) {
            long curTime=gameTime.getTime();
            dt=(curTime-lastUpdateTime)/1000.0;
            lastUpdateTime=curTime;
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            RENDER(dt);
            UPDATE(dt);
            INPUT();
            Display.update();
            Display.sync(100);
            if (Display.isCloseRequested()) {
                Display.destroy();
                System.exit(0);
            }

        }
    }

    /**
     * Initializes the game and creates the screen on which everything is done.
     * @param width the height of the screen.
     * @param height the width of the screen.
     */
    private static void initGL(int width, int height)
    {
        try
        {
            Display.setDisplayMode(new DisplayMode(width,height));
            //Display.setFullscreen(true);
            //Display.setResizable(true);
            //Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
            Display.setTitle("TILEMAP EDITOR - "+name);
            Display.setIcon(new ByteBuffer[] {
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("src\\Assets\\ico16.png")), false, false, null),
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("src\\Assets\\ico32.png")), false, false, null)
            });
            Display.setVSyncEnabled(true);
            Display.create();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            //System.exit(0);
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glViewport(0,0,width,height);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * Ransoms and integer between @param min and @param max
     * @param min minimum value to random.
     * @param max maximum value to random.
     * @return returns a random between @param min and @param max.
     */
    public static int rRn(int min, int max)
    {
        return (int)(Math.random()*max)+min;
    }

    /**
     * Creates an GLimage at the position [ @param x, @param y ] with the image @param path.
     * @param path the path of the image for the GLimage.
     * @param x the x coordinate at which to create the GLimage.
     * @param y the y coordinate at which to create the GLimage.
     * @throws IOException if an invalid file path is specified.
     */
    public static void createImage(String path, int x, int y) throws IOException
    {
        GLimage tex = new GLimage(path,x,y);
        images.add(tex);
        currentTexture+=1;
    }

    /**
     * Creates an GLimage at the position [ @param x, @param y ] with the image @param path adding the @param tag to it
     * @param path the path of the image for the GLimage.
     * @param x the x coordinate at which to create the GLimage.
     * @param y the y coordinate at which to create the GLimage.
     * @param tag the tag to add to the GLimage
     * @throws IOException if an invalid file path is specified.
     */
    public static void createImage(String path, int x, int y, String tag) throws IOException
    {
        GLimage tex = new GLimage(path,x,y,tag);
        images.add(tex);
        currentTexture+=1;
    }

    /**
     * Creates a button on the screen which can be clicked.
     * to detect if a button is being clicked you would access.
     * buttons[0].click;
     * @param normal the normal, deselected picture.
     * @param hover the picture to be shown on hover.
     * @param click the picture to be shown on click.
     * @param x the x position of the button.
     * @param y the y position of the button.
     * @param tag the tag with which your button will be tagged.
     * @throws IOException if you are absolutely crap at specifying one file ...
     */
    public static GLbutton createButton(String normal, String hover, String click, int x, int y, String tag) throws IOException
    {
        GLbutton tex = new GLbutton(normal,hover,click,x,y,tag);
        buttons.add(tex);
        currentButton++;
        return tex;
    }

    public static void createTile(String img, int x, int y, char use, char type) throws IOException
    {
        GLtile tex = new GLtile("tl-"+img,x,y,use,type);
        tex.tag = img;
        tiles.add(tex);
    }

    /**
     * Creates text with a size of @param size containing the string (s).
     * @param s the text to be  displayed
     * @param x the x position of the first letter.
     * @param y the y position of the first letter.
     * @param size changes the size of text, minimum is 0
     * @throws IOException
     */
    public static void createText(String s, int x, int y, int size) throws IOException
    {
        GLtext tex = new GLtext(s,x,y,size);
        text.add(tex);
        currentText++;
    }

    public static GLtile findTile(String tag)
    {
        for(GLtile a :tiles)
        {
            if(("tl-"+a.tag).equals(tag))
            {
                return a;
            }
        }
        return null;
    }
    public static GLtile findTile(char tag)
    {
        for(GLtile a :tiles)
        {
            if((""+a.sm).equals(""+tag))
            {
                return a;
            }
        }
        return tiles.get(0);
    }

    /**
     * This is used to load images into the game before the first update loop.
     * Things like backgrounds.
     * @throws FileNotFoundException if a file not found exception occurs during GLimage creation.
     */
    public static void init() throws FileNotFoundException , AudioControllerException, IOException{
        SET_TILES();
        try {

            createImage("mainBack.png",0,0);
            //createImage("whiteBack.png",50,50);
            //H_thunder a = new H_thunder(10,100,"a");
            //createImage("default",-100,-100);

        } catch (IOException e) {
            e.printStackTrace();
        }

        int q = 0;
        int w = 0;
        for(GLtile[] a : grid)
        {
            for(GLtile l : a)
            {
                //Tools.p(q+":"+w);
                grid[q][w] = new GLtile("tl-default.png",w*32+50,q*32+50,(char)128,(char)128);
                String r = "invisible.png";
                createButton(r,r,r,w*32+50,q*32+50,"-+-:"+w+":"+q);
                w++;
            }
            q++;
            w = 0;
        }

        //this bit finds all files that stary with "tl-" and adds their names to a list to be loaded later
        String current = new java.io.File( "." ).getCanonicalPath();
        File folder = new File(current+"/src/Assets/Art/Tiles");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().startsWith("tl-")) {
                names.add(listOfFiles[i].getName());
            }
        }

        int p1 = 900;
        int p2 = 50;
        GLbutton cur_b;
        int i = 0;
        for(String a:names)
        {
            cur_b = createButton(a,a,a,p1,p2,a);
            createTile(a.substring(3),-100,-100,(char)(tiles.size()+33),'*');
            p2 += 32+10;
            i++;
            if(i == 1)
            {
                tp = cur_b;
            }
            else if(i == names.size())
            {
                bt = cur_b;
            }
        }

        createButton("bUp.png","bUp.png","bUp.png",940,10,"+++");
        createButton("bDown.png","bDown.png","bDown.png",940,700-32-10,"---");
        createButton("bWall.png","bWall.png","bWall.png",860,700,"<>wall");
        createButton("bDoor.png","bDoor.png","bDoor.png",900,700,"<>door");
        createButton("bPass.png","bPass.png","bPass.png",940,700,"<>pass");
        createButton("bSave.png","bSave.png","bSave.png",10,10,"[save]");
        createButton("bLoad.png","bLoad.png","bLoad.png",50,10,"[load]");
        createButton("bNew.png","bNew.png","bNew.png",10,700,"[new]");
        createButton("bBkt.png","bBkt.png","bBkt.png",300,10,"<>bucket");
        createButton("bPen.png","bPen.png","bPen.png",340,10,"<>pen");
        curSel = tiles.get(0);

    }

    public static void SET_TILES() throws IOException
    {
        //createTile("dirt.png",0,0,'d','*');
        //reateTile("grass.png",0,0,'g','*');
    }

    /**
     * Where you do things to GLimages to make the game function.
     * Yes I know it's vague but that is what it is.
     * @throws IOException if a file not found exception occurs during GLimage creation.
     */
    public static void UPDATE(double dt) throws IOException
    {
        int num = Mouse.getDWheel();
        if(num != 0)
        {
            for(GLbutton b : buttons)
            {
                if(b.tag.startsWith("tl-"))
                {
                    b.y+=num/10;
                }
            }
        }


        for(GLbutton a: buttons)
        {
            if(a.click)
            {
                //Tools.p(a.tag);
                if(a.tag.startsWith("tl-"))
                {
                    curSel = findTile(a.tag);
                }
                if(a.tag.startsWith("-+-:"))
                {
                    String[] k = a.tag.split(":");
                    int gx = Integer.parseInt(k[1]);
                    int gy = Integer.parseInt(k[2]);
                    GLtile d = curSel;
                    GLtile q = new GLtile("tl-"+d.tag,gx*32+50,gy*32+50,d.sm,d.tp);
                    grid[gy][gx] = q;
                }
                if(a.tag.startsWith("+++"))
                {
                    for(GLbutton b : buttons)
                    {
                        if(b.tag.startsWith("tl-"))
                        {
                            b.y-=3;
                        }
                    }
                }
                if(a.tag.startsWith("---"))
                {

                    for(GLbutton b : buttons)
                    {
                        if(b.tag.startsWith("tl-"))
                        {
                            b.y+=3;
                        }
                    }
                }
                if(a.tag.startsWith("<>w"))
                {
                    curSel.tp = '#';
                }
                if(a.tag.startsWith("<>d"))
                {
                    curSel.tp = '@';
                }
                if(a.tag.startsWith("<>p"))
                {
                    curSel.tp = '*';
                }
                if(a.tag.startsWith("[save]"))
                {
                    SAVE();
                }
                if(a.tag.startsWith("[load]"))
                {
                    LOAD();
                }
                if(a.tag.startsWith("[new]"))
                {
                    int q = 0;
                    int w = 0;
                    for(GLtile[] b : grid)
                    {
                        for (GLtile l : b)
                        {
                            grid[q][w] = new GLtile("tl-default.png", w * 32 + 50, q * 32 + 50, (char) 1, (char) 1);
                            w++;
                        }
                        q++;
                        w = 0;
                    }
                }
                if(a.tag.startsWith("<>pen"))
                {
                    tool = "pen";
                }
                if(a.tag.startsWith("<>bucket"))
                {
                    tool = "bucket";
                }
            }
        }
    }

    public static void SAVE() throws FileNotFoundException, UnsupportedEncodingException {
        //Tools.p("Saving ...");
        String path = "src\\Assets\\Scenes\\";
        PrintWriter out = new PrintWriter(path+name+".map","UTF-8");
        PrintWriter out2 = new PrintWriter(path+name+".col","UTF-8");
        //Tools.p("Writing header ...");

        //out.println();
        //Tools.p("Writing content ...");
        for(GLtile[] a : grid)
        {
            for(GLtile b : a)
            {
                out.print(""+b.sm);
                out2.print(""+b.tp);
            }
            out.println();
            out2.println();
        }
        //Tools.p("Saved.");
        //Tools.p("-> "+path+name);
        out.close();
        out2.close();
    }

    public static void LOAD() throws IOException {
        String path = "src\\Assets\\Scenes\\";
        Scanner in = new Scanner(new FileReader(path+name+".map"));
        Scanner in2 = new Scanner(new FileReader(path+name+".col"));
        //[ed] end of header object
        //[cv] divisor between char and name of header obj
        //[sp] divisor between grids (this one is just in case a char get out of hand)
        //String header = in.nextLine();

        int q = 0;
        int w = 0;
        for(GLtile[] a : grid)
        {
            String s = in.nextLine();
            String s2 = in2.nextLine();
            char[] sp = s.toCharArray();
            char[] sp2 = s2.toCharArray();
            for(GLtile b : a)
            {
                GLtile t = findTile(sp[w]);
                //System.out.println(t);
                b = new GLtile("tl-"+t.tag,w*32+50,q*32+50,sp[w],sp2[w]);
                grid[q][w] = b;
                w++;
            }
            w=0;
            q++;
        }

    }

    /**
     * Renders all GLimages in images[] and runs their updates their information.
     */
    public static void RENDER(double dt) throws IOException , AudioControllerException
    {

        //System.out.println(dt);
        for(int i = 0;  i< images.size(); i++)
        {
            images.get(i).render();
            images.get(i).varUpdate(images, currentTexture);
        }
        for(int i = 0;  i< buttons.size(); i++)
        {
            buttons.get(i).render();
            buttons.get(i).update(Mouse.getX(),Mouse.getY(),Mouse.isButtonDown(0),dt);
        }
        for(int i = 0;  i< text.size(); i++)
        {
            text.get(i).render();
        }

        for(GLtile[] a : grid)
        {
            for(GLtile l : a)
            {
                l.render();
            }
        }
    }

    /**
     * You casual main(String [] args) function
     * Starts the game ...
     * @param args ... they are args ...
     * @throws IOException if you suck at specifying file paths...
     */
    public static void main(String[] args) throws IOException , AudioControllerException
    {
        gameTime.start();
        start();
    }

    /**
     * Controls user input.
     * Mouse and Keyboard.
     */
    public static void INPUT() throws IOException , AudioControllerException
    {

        if (Mouse.isButtonDown(0))
        {
            //Tools.p(""+Mouse.getX()+" : "+(H-Mouse.getY()));
        }
        if (Mouse.isButtonDown(1)) {

        }

    }

}

