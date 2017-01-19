import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bodyi on 1/6/2017.
 */
public class GLplayer {
    double rate;
    private Time shootTimer=new Time();
    boolean shooting = false;
    double x;
    double y;
    String pth = "Assets\\Art\\Tiles\\";
    Texture texture;
    double health;
    double speed;
    ArrayList<Texture> dir = new ArrayList<>();
    String facing = "down";
    ArrayList<String> names = new ArrayList<>();
    ArrayList<GLweapon> patt = new ArrayList<>();
    GLweapon curWeapon = new GLweapon("a2:3:!#f10=-.e");
    public GLplayer(int x, int y, double health, double speed, double rate) throws IOException
    {
        this.rate = rate;
        shootTimer.start();
        this.x = x;
        this.health = health;
        this.speed = speed;
        this.y = y;
        String current = new java.io.File( "." ).getCanonicalPath();
        File folder = new File(current+"/src/Assets/Art/Tiles");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().startsWith("P-")) {
                names.add(listOfFiles[i].getName());
            }
        }
        int a = 0;
        for( String s : names)
        {
            Texture e = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pth+s));
            dir.add(e);
            a++;
        }
    }
    public void setTex()
    {
        texture = getImg(facing);
    }

    public Texture getImg(String fce)
    {
        fce = "P-"+fce+"1.png";
        int idx =0;
        for(String s : names)
        {
            if(s.equals(fce))
            {
                return dir.get(idx);
            }
            idx++;
        }
        return dir.get(idx);
    }

    public void render() throws IOException
    {
        setTex();
        Color.white.bind();
        texture.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0,0);
        GL11.glVertex2f((int)x,(int)y);
        GL11.glTexCoord2f(1,0);
        GL11.glVertex2f((int)x+texture.getTextureWidth(),(int)y) ;
        GL11.glTexCoord2f(1,1);
        GL11.glVertex2f((int)x+texture.getTextureWidth(),(int)y+texture.getTextureHeight());
        GL11.glTexCoord2f(0,1);
        GL11.glVertex2f((int)x,(int)y+texture.getTextureHeight());
        GL11.glEnd();
        updt();
        //curWeapon.render();
    }

    public void updt()
    {
        if(shooting && shootTimer.getTime()>rate)
        {
            shoot();
            shootTimer.clear();
            shootTimer.start();
        }
    }

    public void shoot()
    {

    }
}
