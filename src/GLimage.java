import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that controls sprites
 * Can i get any more vague ... i think i can.
 * @author Andrey Grebenik (Kuro)
 * @version 1.0
 * @since 2016-11-19
 */

public class GLimage extends Component
{

	private AudioController a;
	// the texture.
	private Texture texture;
	// special id that is given to every object.
	int identity;
	// x and y position.	
	int x = 0;
	int y = 0;

	int enl = 0;

	// vertical and horisontal stretch
	int sV = 0;
	int sH = 0;
	// texture height and width.
	int height;
	int width;
	// the tag that the GLimage has/has not been given
	String tag = "default.tag";
	// Temporary variable slots.
	int[] tempInt = new int[100];
	String[] tempStr = new String[100];
	boolean[] tempBol = new boolean[100];


	boolean normal = true;

	// list of all GLimages so the GLimage can know where what is.
	private ArrayList<GLimage> images = new ArrayList<>();
	//the filepath of the current image.
	public String path = "";
	private int max;
	
	/**
	 * When the GLimage is initialized this is called. 
	 * Creates a GLimage at [ @param Px, @param Py ] with an image of @param path.
	 * @param path the image path of the GLimage
	 * @param Px the start x position.
	 * @param Py the start y position.
	 * @throws IOException if you are a complete idiot and somehow can't get your filepaths right.
	 */
	public GLimage(String path, int Px, int Py) throws IOException
	{
		this.x = Px;
		this.y = Py;
		this.path = path;
		this.tag = "default";
		init();
	}
	public GLimage(String path, int Px, int Py, boolean letter) throws IOException
	{
		this.x = Px;
		this.y = Py;
		this.path = path;
		this.normal = !letter;
		this.tag = "default";
		init();
	}
	
	/**
	 * When the GLimage is initialized this is called. 
	 * Creates a GLimage at [ @param Px, @param Py ] with an image of @param path.
	 * @param path the image path of the GLimage
	 * @param Px the start x position.
	 * @param Py the start y position.
	 * @param tag the tag with which the object is going to be tagged.
	 * @throws IOException if you are a complete idiot and somehow can't get your filepaths right.
	 */
	public GLimage(String path, int Px, int Py, String tag) throws IOException
	{
		this.tag = tag;
		this.x = Px;
		this.y = Py;
		this.path = path;

		init();
	}
	
	/**
	 * Loads the image and sets width and height of it. 
	 * @throws IOException if you can't bother to change your '\'to a '\\' or a '/'
	 */
	public void init() throws IOException
	{
		String pth = "";
		if(normal)
		{
			pth = "Assets\\Art\\Tiles\\";
		}
		try
		{
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+path));
		}
		catch(Exception e)
		{
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+"default.png"));
		}

		height = texture.getImageHeight();
		width = texture.getImageWidth();
		Random rand = new Random();
		this.identity = rand.nextInt();
		a = new AudioController();
		
	}
	
	/**
	 * Updates the the images and max for the GLimage.
	 * Ha! i think this is more vague than the class description.
	 * @param images the GLimage list to be used as an existing sprite reference.
	 * @param max the next index of the GLimage to be ever existent in case you want to create one. 
	 */
	public void varUpdate(ArrayList<GLimage> images,int max)
	{
		this.images = images;
		this.max = max;
	}
	
	/**
	 * render the GLimage on the screen at [ @param x, @param y ].
	 */
	public void render()
	{
			Color.white.bind();
			texture.bind();
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0,0);
			GL11.glVertex2f(x-enl,y-enl);
			GL11.glTexCoord2f(1,0);
			GL11.glVertex2f(x+texture.getTextureWidth()+sH+enl,y-enl) ;
			GL11.glTexCoord2f(1,1);
			GL11.glVertex2f(x+texture.getTextureWidth()+sH+enl,y+texture.getTextureHeight()+sV+enl);
			GL11.glTexCoord2f(0,1);
			GL11.glVertex2f(x-enl,y+texture.getTextureHeight()+sV+enl);
			GL11.glEnd();
	}
	
	/**
	 * return an array of all GLimages with a tag of @param tag and within range of @param rage.
	 * @param tag the tag of the GLimages to search for, null for ones without a tag.
	 * @param range the range the GLimags have to be away from you to be recognized (pls notice me senpai).
	 * @return returns an array of all GLimages within @param range;
	 */
	public GLimage[] inRange(String tag, int range)
	{
		double rng = 0;
		GLimage[] all = new GLimage[10000];
		int it = 0;
		for(int i = 0; i < this.images.size(); i++)
		{
			rng = rangeTo(this.images.get(i));
			if(rng > range && this.images.get(i).tag.equals(tag))
			{
				all[it] = this.images.get(i);
				it += 1;
			}
		}
		return all;
	}
	
	/**
	 * tells you the range of this GLimage to GLimage @param other 
	 * @param other the GLimage to which the distance needs to be calculated
	 * @return returns the distance to the GLimage.
	 */
	private double rangeTo(GLimage other) {
		double x1 = this.x + this.texture.getTextureWidth() / 2;
		double x2 = other.x + other.texture.getTextureWidth() / 2;
		double y1 = this.y + this.texture.getTextureHeight() / 2;
		double y2 = other.y + other.texture.getTextureHeight() / 2;
		double dx = Math.abs(x1 - x2);
		double dy = Math.abs(y1 - y2);
		double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		return dist;
	}

	public void enlarge(int size)
	{
		this.enl = size;
	}

	public void stretch(int width, int height)
	{
		this.sV = height;
		this.sH = width;
	}

	public void addFX(String path, String name) throws IOException, AudioControllerException
	{
		a.addSound("Assets\\Audio\\SFX\\"+path, name+".sf");
		//Tools.p("loaded FX > Assets\\Audio\\SFX\\"+path+" as "+name+".sf");
	}

	public void playFX(String name,double volume) throws AudioControllerException
	{
		a.playSoundEffect(name+".sf",volume);
		//Tools.p("playing FX > "+name+".sf");
	}

	public void addMU(String path, String name) throws IOException, AudioControllerException
	{
		a.addSound("Assets\\Audio\\BGM\\"+path, name+".mu");
		//Tools.p("loaded MU > Assets\\Audio\\SFX\\"+path+" as "+name+".mu");
	}

	public void playMU(String name,double volume) throws AudioControllerException
	{
		a.playMusic(name+".mu",volume,true);
		//Tools.p("playing MU > "+name+".mu");
	}
}
