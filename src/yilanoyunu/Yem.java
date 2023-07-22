
package yilanoyunu;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Yem extends JLabel
{
    public int mGenislik = 20;
    private Image elma;
    
    public Yem() 
    {
        setPosition(300, 240);
        ImageIcon img = new ImageIcon("resim/elma3.png");
        elma= img.getImage();
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D)g;
        Ellipse2D elipse = new Ellipse2D.Double(1, 1, mGenislik-2, mGenislik-2);
        
        g2.setStroke(new BasicStroke(2));
        
        g2.drawImage(elma, 0, 0, null);
        //g2.fillOval(0, 0, 20, 20);
    }
    public void setPosition(int Posx, int PosY)
    {
        setBounds(Posx, PosY, mGenislik, mGenislik);
    }
}
