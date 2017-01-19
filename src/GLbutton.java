import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import java.io.*;

import java.util.Random;

/**
 * This is the button class, creates a clickable button.
 * @author Andrey Grebenik (Kuro)
 * @version 1.0
 * @since 2016-11-19
 */
public class GLbutton {

    private AudioController a;

    private Texture b_normal;
    private Texture b_hover;
    private Texture b_click;

    public String n_path;
    public String h_path;
    public String c_path;

    public boolean hold = false;
    private boolean holding = false;

    private Texture currentTex;

    int identity;

    int x = 0;
    int y = 0;

    int sV = 0;
    int sH = 0;

    int enl = 0;

    int height;
    int width;

    boolean hover;
    boolean click;

    int mouseX;
    int mouseY;

    String tag = "default.tag";

    /**
     * creates a button
     * @param normal the frame that will be displayed when the button is not selected or clicked.
     * @param Px x position of button.
     * @param Py y position of button.
     * @param tag the tag of this button.
     * @throws IOException
     */
    public GLbutton(String normal, int Px, int Py, String tag) throws IOException{

        this.x = Px;
        this.y = Py;
        this.tag = tag;
        this.n_path = normal;
        Tools.p("loading button ...");
        String pth = "Assets\\Art\\Tiles\\";
        b_normal = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+normal));
        b_hover = b_normal;
        b_click = b_normal;
        Tools.p("ld >> "+normal);
        height = b_normal.getImageHeight();
        width = b_normal.getImageWidth();
        init();

    }

    /**
     * creates a button
     * @param normal the frame that will be displayed when the button is not selected or clicked.
     * @param hover when you hover over the button this frame will  be  displayed.
     * @param Px x position of button.
     * @param Py y position of button.
     * @param tag the tag of this button.
     * @throws IOException
     */
    public GLbutton(String normal, String hover, int Px, int Py, String tag) throws IOException{
        this.x = Px;
        this.y = Py;
        this.tag = tag;
        this.n_path = normal;
        this.h_path = hover;
        String pth = "Assets\\Art\\Tiles\\";
        Tools.p("loading button ...");
        b_normal = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+normal));
        Tools.p("ld >> "+normal);
        b_hover = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+hover));
        Tools.p("ld >> "+hover);
        b_click = b_normal;
        height = b_normal.getImageHeight();
        width = b_normal.getImageWidth();
        init();
    }

    /**
     * creates a button
     * @param normal the frame that will be displayed when the button is not selected or clicked.
     * @param hover when you hover over the button this frame will  be  displayed.
     * @param click if you click this frame wi ll be displayed.
     * @param Px x position of button.
     * @param Py y position of button.
     * @param tag the tag of this button.
     * @throws IOException
     */
    public GLbutton(String normal,String hover, String click, int Px, int Py, String tag) throws IOException{
        this.x = Px;
        this.y = Py;
        this.tag = tag;
        this.n_path = normal;
        this.h_path = hover;
        this.c_path = click;
        //Tools.p("loading button ...");
        String pth = "Assets\\Art\\Tiles\\";
        b_normal = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+normal));
        //Tools.p("ld >> "+normal);
        b_hover = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+hover));
        //Tools.p("ld >> "+hover);
        b_click = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+click));
        //Tools.p("ld >> "+click);
        height = b_normal.getImageHeight();
        width = b_normal.getImageWidth();
        init();
    }

    /**
     * initializes the button's variables
     * @throws IOException if you cant input one damn filename right.
     */
    public void init() throws IOException
    {
        Random rand = new Random();
        this.identity = rand.nextInt();
        a = new AudioController();
        currentTex = b_normal;

    }

    /**
     *updates the coordinates for mouse x and mouse y.
     * @param mX
     * @param mY
     * @param c
     */
    public void update(int mX, int mY, boolean c, double dt)
    {
        mouseX = mX;
        mouseY = Display.getHeight()-mY;
        height = currentTex.getImageHeight();
        width = currentTex.getImageWidth();
        if(click && holding)
        {
            holding = false;
        }
        if(mouseInBox() && c && !holding)
        {
            this.click = true;
            currentTex = b_click;
            enlarge(2);
            if(hold)
            {
                holding = true;
            }
            render();
        }
        else if(mouseInBox()&& !holding)
        {
            this.hover = true;
            this.click = false;
            currentTex = b_hover;
            enlarge(1);
            render();
        }
        else if(!holding)
        {
            this.click = false;
            this.hover = false;
            currentTex = b_normal;
            enlarge(0);
            render();
        }
    }

    /**
     * renders the button adding stretches and enlarges
     */
    public void render()
    {
        Color.white.bind();
        currentTex.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0,0);
        GL11.glVertex2f(x-enl,y-enl);
        GL11.glTexCoord2f(1,0);
        GL11.glVertex2f(x+currentTex.getTextureWidth()+sH+enl,y-enl) ;
        GL11.glTexCoord2f(1,1);
        GL11.glVertex2f(x+currentTex.getTextureWidth()+sH+enl,y+currentTex.getTextureHeight()+sV+enl);
        GL11.glTexCoord2f(0,1);
        GL11.glVertex2f(x-enl,y+currentTex.getTextureHeight()+sV+enl);
        GL11.glEnd();
    }

    /**
     *
     * @param size
     */
    public void enlarge(int size)
    {
        this.enl = size;
    }

    /**
     *
     * @param width
     * @param height
     */
    public void stretch(int width, int height)
    {
        this.sV = height;
        this.sH = width;
    }

    public void Hold()
    {
        hold = true;
    }

    /**
     *
     * @return
     */
    public boolean mouseInBox()
    {
        if((mouseX >= this.x && mouseX <= this.x + width + sH)&&(mouseY >= this.y && mouseY <= this.y + height + sV)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param path
     * @param name
     * @throws IOException
     * @throws AudioControllerException
     */
    public void addFX(String path, String name) throws IOException, AudioControllerException
    {
        a.addSound("Assets\\Audio\\SFX\\"+path, name+".sf");
    }

    /**
     *
     * @param name
     * @throws AudioControllerException
     */
    public void playFX(String name,double volume) throws AudioControllerException
    {
        a.playSoundEffect(name+".sf",volume);
    }
}
