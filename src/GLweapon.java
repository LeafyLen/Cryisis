import java.io.IOException;
import java.nio.channels.InterruptibleChannel;
import java.util.ArrayList;

/**
 * Created by bodyi on 1/6/2017.
 */
public class GLweapon {
    private Time delay=new Time();
    /**
     * Pattern rules:
     * numbers: "1-9" show how many bullets come out followed by a ':' and the damage ending in another ':' 1:10:
     * symbols: "-" extends the life of the bullet by 0.3 of a second
     * symbols: "." extends the life of the bullet by 0.1 of a second
     * symbols: "=" extends the life of the bullet by 1 second
     * letters: "a" start sequence (followed by num bullets allways)
     * letters: "e" ends the sequence
     * letters: "f,b,l,r" add/subtract horizontal or vertical speed
     * letters: "S" to remove all velocity
     * patterns: after the a1:10: you specify the pattern ending it with an '#'
     * patterns: ")" spreads all bullets out
     * patterns: "!" straight line
     * patterns: "^" makes a + of bullets  spreading out
     * patterns: "@" bullets spin around point
     * patterns: "~" to create a sine wave, comma to add period and amplitude (in pixels) (:~10,1)
         *
     * examples: a2:3:!#f10=-.e
     *      creates 2 bullets side by side and fires them at a speed of 10 pixels a second for 1.4 seconds
     * examples: a5:1:)#f3--S=f10==-e
     */
    String pattS = "";
    char[] patt;
    int curP = 0;
    char cur;
    int wait = 0;
    boolean active;
    String step = "init";
    String pattern = "";
    String fcg = "up";
    int amt = 0;
    int dmg = 0;
    int x = 0;
    int y = 0;
    ArrayList<GLbullet> bullets = new ArrayList<>();


    public GLweapon(String rule)
    {
        pattS = rule;
        curP = 0;
        patt = pattS.toCharArray();
        cur = patt[curP];
    }

    public void upPtt() throws IOException
    {
        if(wait < 1)
        {
            if(step.equals("create"))
            {
                String[] rr = curveSp(1,1,0);
                if(pattern.equals("spread"))
                {
                    rr = curveSp(45,amt, 1);
                }
                else if (pattern.equals("radial"))
                {
                    rr = curveSp(360,amt,1);
                }
                for(int i = 0; i < amt; i++)
                {
                    if(pattern.equals("straight"))
                    {
                        GLbullet b = new GLbullet(x,y);
                        if(fcg.equals("up")) {
                            b.dX = 0;
                            b.dY = -1;
                        } else if(fcg.equals("down")) {
                            b.dX = 0;
                            b.dY = 1;
                        } else if(fcg.equals("left")) {
                            b.dY = 0;
                            b.dX = -1;
                        } else if(fcg.equals("right")) {
                            b.dY = 0;
                            b.dX = -1;
                        } else {
                            b.dX = 0;
                            b.dY = 0;
                        }
                    }
                    if(pattern.equals("spread"))
                    {
                        for(int d = 0; d<rr.length; d++)
                        {
                            GLbullet b = new GLbullet(x,y);
                            String[] q = rr[d].split(",");
                            b.dX = Integer.parseInt(q[0]);
                            b.dY = Integer.parseInt(q[1]);
                            bullets.add(b);
                        }
                    }
                    if(pattern.equals("radial"))
                    {
                        for(int d = 0; d<rr.length; d++)
                        {
                            GLbullet b = new GLbullet(x,y);
                            String[] q = rr[d].split(",");
                            b.dX = Integer.parseInt(q[0]);
                            b.dY = Integer.parseInt(q[1]);
                            bullets.add(b);
                        }
                    }
                }
                step = "finished";
            }
            if(step.equals("pat"))
            {
                curP++;
                cur = patt[curP];
                if((cur+"").equals("!"))
                {
                    pattern = "straight";
                }
                else if((cur+"").equals(")"))
                {
                    pattern = "spread";
                }
                else if((cur+"").equals("^"))
                {
                    pattern = "radial";
                }
                step = "create";
            }
            if(step.equals("dmg"))
            {
                curP++;
                cur = patt[curP];
                dmg = Integer.parseInt(cur+"");
                step = "pat";
                Tools.p("> damage "+dmg);
            }
            if(step.equals("num"))
            {
                try
                {
                    int a = (Integer.parseInt(""+cur));
                    if(!(patt[curP+1]+"").equals(":"))
                    {
                        curP++;
                        cur = patt[curP];
                        int b = (Integer.parseInt(""+cur));
                        amt = a*10 + b;
                    }
                    else
                    {
                        amt = a;
                    }
                    step = "dmg";
                    Tools.p("> number "+amt);
                }
                catch (Exception e)
                {

                }
            }
            if((""+cur).equals("a"))
            {
                active = true;
                step = "num";
                Tools.p(">init");
            }
            if((""+cur).equals("e"))
            {
                active = false;
                for(GLbullet a : bullets)
                {
                    a.stop();
                }
            }
            if((""+cur).equals("="))
            {
                wait = 1000;
                delay.start();
            }
            if((""+cur).equals("-"))
            {
                wait = 300;
                delay.start();
            }
            if((""+cur).equals("."))
            {
                wait = 100;
                delay.start();
            }
            curP++;
            cur = patt[curP];
        }
        if(wait > 0)
        {
            if(delay.getTime()>wait)
            {
                wait = 0;
                delay.clear();
            }
        }
    }

    public String[] curveSp(int angle, int points, double speed)
    {
        double sub = angle/points;
        double top = angle/2;
        String[] re = new String[points];
        for(int i =0; i < points; i++)
        {
            double[] rs = diff(top,speed);
            String wr = rs[0]+","+rs[1];
            re[i] = wr;
            top -= sub;
        }
        return re;
        
    }

    public double[] diff(double ang, double speed)
    {

        double h = speed;
        double dx = Math.sin(ang)*h;
        double dy = Math.cos(ang)*h;
        double[] rt = {dx,dy};
        return rt;
    }
    public void render() throws IOException
    {
        for(GLbullet b : bullets)
        {
            b.render();
            b.x += b.dX;
            b.y += b.dY;
        }
        upPtt();
        Tools.p(step+","+curP+" >> "+cur+" - "+pattern);
    }

}
