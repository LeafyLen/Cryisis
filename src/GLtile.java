import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

/**
 * Created by bodyi on 1/3/2017.
 */
public class GLtile {
    private String image;
    String pth = "Assets\\Art\\Tiles\\";
    private Texture texture;
    public char tp = '*';
    // # - wall
    // @n - nth door
    // * - empty tile
    public char sm;
    public int x = 0;
    public int y = 0;
    public int h;
    public int w;
    public String tag;
    public GLtile(String path,int x, int y, char symbol, char type) throws IOException
    {
        sm = symbol;
        tp = type;
        this.x = x;
        this.y = y;
        texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+path));
        h = texture.getTextureHeight();
        w = texture.getTextureWidth();
    }

    public void render()
    {
        Color.white.bind();
        texture.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0,0);
        GL11.glVertex2f(x,y);
        GL11.glTexCoord2f(1,0);
        GL11.glVertex2f(x+texture.getTextureWidth(),y) ;
        GL11.glTexCoord2f(1,1);
        GL11.glVertex2f(x+texture.getTextureWidth(),y+texture.getTextureHeight());
        GL11.glTexCoord2f(0,1);
        GL11.glVertex2f(x,y+texture.getTextureHeight());
        GL11.glEnd();
    }
}
