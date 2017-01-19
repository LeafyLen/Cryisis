import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bodyi on 1/10/2017.
 */
public class GLbullet {
    int x;
    int enl = 0;
    int y;
    double dX;
    double dY;
    double damage;
    boolean moving = false;
    String pth = "Assets\\Art\\Tiles\\";
    ArrayList<Texture> frames = new ArrayList<>();
    public GLbullet(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public void vel(double dx, double dy)
    {
        dX = dx;
        dY = dy;
        moving = true;
    }
    public void stop()
    {
        dX = 0;
        dY = 0;
        moving = false;
    }
    public void setFrames(String path) throws IOException
    {
        frames.add(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+"b-1.png")));
    }

    public void render()
    {
        Texture texture = frames.get(0);
        Color.white.bind();
        texture.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0,0);
        GL11.glVertex2f(x-enl,y-enl);
        GL11.glTexCoord2f(1,0);
        GL11.glVertex2f(x+texture.getTextureWidth()+enl,y-enl) ;
        GL11.glTexCoord2f(1,1);
        GL11.glVertex2f(x+texture.getTextureWidth()+enl,y+texture.getTextureHeight()+enl);
        GL11.glTexCoord2f(0,1);
        GL11.glVertex2f(x-enl,y+texture.getTextureHeight()+enl);
        GL11.glEnd();
    }

    public void enlarge(int s)
    {
        enl = s;
    }
}
