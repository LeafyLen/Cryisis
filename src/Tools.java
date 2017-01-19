import java.awt.*;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.net.InetAddress;
import java.util.Scanner;

public class Tools {
    public static String i(String pt) {
        Scanner sc = new Scanner(System.in);
        System.out.print(pt);
        return sc.nextLine();
    }

    public static void p(double[] ls) {
        String st = "db[";
        for (int i = 0; i < ls.length; i++) {
            if (ls.length - i == 1) {
                st += (" " + ls[i] + " ]");
            } else {
                st += (" " + ls[i] + ",");
            }
        }
        System.out.println(st);

    }

    public static void p(String[] ls) {
        String st = "st[";
        for (int i = 0; i < ls.length; i++) {
            if (ls.length - i == 1) {
                st += (" \"" + ls[i] + "\" ]");
            } else {
                st += (" \"" + ls[i] + "\",");
            }
        }
        System.out.println(st);

    }

    public static void p(int[] ls) {
        String st = "it[";
        for (int i = 0; i < ls.length; i++) {
            if (ls.length - i == 1) {
                st += (" " + ls[i] + " ]");
            } else {
                st += (" " + ls[i] + ",");
            }
        }
        System.out.println(st);

    }

    public static void p(float[] ls) {
        String st = "fl[";
        for (int i = 0; i < ls.length; i++) {
            if (ls.length - i == 1) {
                st += (" " + ls[i] + " ]");
            } else {
                st += (" " + ls[i] + ",");
            }
        }
        System.out.println(st);

    }

    public static void p(byte[] ls) {
        String st = "bt[";
        for (int i = 0; i < ls.length; i++) {
            if (ls.length - i == 1) {
                st += (" " + ls[i] + " ]");
            } else {
                st += (" " + ls[i] + ",");
            }
        }
        System.out.println(st);

    }

    public static void p(char[] ls) {
        String st = "ch[";
        for (int i = 0; i < ls.length; i++) {
            if (ls.length - i == 1) {
                st += (" '" + ls[i] + "' ]");
            } else {
                st += (" '" + ls[i] + "',");
            }

        }
        System.out.println(st);

    }



    public static void p(String ls) {
        System.out.println(ls);
    }

    public static void p(int ls) {
        System.out.println(ls);
    }

    public static void p(float ls) {
        System.out.println(ls);
    }

    public static void p(double ls) {
        System.out.println(ls);
    }

    public static void p(byte ls) {
        System.out.println(ls);
    }

    public static void p(char ls) {
        System.out.println(ls);
    }

    public static void p(boolean ls) {
        System.out.println(ls);
    }

    public static void p(StringBuffer ls) { System.out.println(ls); }

    public static void send(OutputStreamWriter osw, String message) throws IOException {
        osw.write(message + (char) 13);
        osw.flush();

    }

    public static StringBuffer recive(InputStreamReader isr) throws IOException {
        StringBuffer instr = new StringBuffer();

        instr = new StringBuffer();
        int c;
        while ((c = isr.read()) != 13) {
            if (c > 0) {
                instr.append((char) c);
            }
        }
        return instr;
        //connection.close();
    }

    public static void p(InetAddress localHost) {
        System.out.println(localHost);

    }

    public static String[] colS(int identity) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/proj01/col/" + identity + ".txt"));
        String line = br.readLine();
        String[] lines = line.split("~");
        return lines;
    }

    public static String[][] pi(String inFile) {

        Image image = Toolkit.getDefaultToolkit().getImage(inFile);
        try {
            PixelGrabber grabber = new PixelGrabber(image, 0, 0, -1, -1, false);

            if (grabber.grabPixels()) {
                int width = grabber.getWidth();
                int height = grabber.getHeight();
                String[] res = new String[3];
                String[][] tr = new String[(width * height) + 2][4];
                tr[0][0] = width + "";
                tr[1][0] = height + "";

                if (isGreyscaleImage(grabber)) {
                    byte[] data = (byte[]) grabber.getPixels();
                } else {
                    int[] data = (int[]) grabber.getPixels();
                    for (int i = 0; i < data.length; i++) {
                        tr[i + 2][0] = toC(data[i])[0] + "";
                        tr[i + 2][1] = toC(data[i])[1] + "";
                        tr[i + 2][2] = toC(data[i])[2] + "";
                        tr[i + 2][3] = toC(data[i])[3] + "";
                    }
                    return tr;
                }
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public static final boolean isGreyscaleImage(PixelGrabber pg) {
        return pg.getPixels() instanceof byte[];
    }

    public static int[] toC(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        int[] a = {alpha, red, green, blue};
        return a;
    }

    public static boolean inList(String s)
    {
        String[] chars = {"q","w","e","r","t","y","u","i","o","p","a","s","d","f","g","h","j","k","l","z","x","c","v","b","n","m","1",
                          "2","3","4","5","6","7","8","9","0"};
        for(int i = 0; i < chars.length; i++)
        {
            //Tools.p("cp |["+ s +" :-: "+ chars[i] + "]|");
            if(chars[i].equals(s))
            {
                //Tools.p("mf > "+chars[i]+" : "+s);
                return true;
            }
        }
        return false;
    }

    public static String toLet(String a) {
        String ret = "";
        if(inList(a))
        {
            ret = "Assets\\Font\\"+a.toUpperCase()+".png";
        }
        else if(a.equals("`"))
        {
            ret = "Assets\\Font\\accent.png";
        }

        else if(a.equals("&"))
        {
            ret = "Assets\\Font\\and.png";
        }
        else if(a.equals("*"))
        {
            ret = "Assets\\Font\\ast.png";
        }
        else if(a.equals("@"))
        {
            ret = "Assets\\Font\\at.png";
        }
        else if(a.equals("\\"))
        {
            ret = "Assets\\Font\\bslash.png";
        }
        else if(a.equals("^"))
        {
            ret = "Assets\\Font\\carrot.png";
        }
        else if(a.equals(":"))
        {
            ret = "Assets\\Font\\col.png";
        }
        else if(a.equals(","))
        {
            ret = "Assets\\Font\\comma.png";
        }
        else if(a.equals("$"))
        {
            ret = "Assets\\Font\\doll.png";
        }
        else if(a.equals("."))
        {
            ret = "Assets\\Font\\dot.png";
        }
        else if(a.equals("\""))
        {
            ret = "Assets\\Font\\dquot.png";
        }
        else if(a.equals("="))
        {
            ret = "Assets\\Font\\eq.png";
        }
        else if(a.equals("!"))
        {
            ret = "Assets\\Font\\exclaim.png";
        }
        else if(a.equals("/"))
        {
            ret = "Assets\\Font\\fslash.png";
        }
        else if(a.equals(">"))
        {
            ret = "Assets\\Font\\greater.png";
        }
        else if(a.equals("<"))
        {
            ret = "Assets\\Font\\less.png";
        }
        else if(a.equals("-"))
        {
            ret = "Assets\\Font\\minus.png";
        }
        else if(a.equals("#"))
        {
            ret = "Assets\\Font\\num.png";
        }
        else if(a.equals("%"))
        {
            ret = "Assets\\Font\\percent.png";
        }
        else if(a.equals("|"))
        {
            ret = "Assets\\Font\\pipe.png";
        }
        else if(a.equals("+"))
        {
            ret = "Assets\\Font\\plus.png";
        }
        else if(a.equals("?"))
        {
            ret = "Assets\\Font\\question.png";
        }
        else if(a.equals("'"))
        {
            ret = "Assets\\Font\\quot.png";
        }
        else if(a.equals("("))
        {
            ret = "Assets\\Font\\quote0.png";
        }
        else if(a.equals(")"))
        {
            ret = "Assets\\Font\\quote1.png";
        }
        else if(a.equals("["))
        {
            ret = "Assets\\Font\\quote2.png";
        }
        else if(a.equals("]"))
        {
            ret = "Assets\\Font\\quote3.png";
        }
        else if(a.equals("{"))
        {
            ret = "Assets\\Font\\quote4.png";
        }
        else if(a.equals("}"))
        {
            ret = "Assets\\Font\\quote5.png";
        }
        else if(a.equals(";"))
        {
            ret = "Assets\\Font\\semicol.png";
        }
        else if(a.equals(" "))
        {
            ret = "Assets\\Font\\space.png";
        }
        else if(a.equals("~"))
        {
            ret = "Assets\\Font\\tilda.png";
        }
        else if(a.equals("_"))
        {
            ret = "Assets\\Font\\under.png";
        }
        else
        {
            ret = "Assets\\Font\\err.png";
        }
        return ret;
    }

}
