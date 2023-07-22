
package yilanoyunu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.Rectangle2D;
import javax.swing.JLabel;



public class Kutu extends JLabel
{

    public int mGenislik = 20;
    
    public int mYon = Yon.SAG;
    
    
    
    public Kutu() 
    {
        this.setBounds(0, 240, mGenislik, mGenislik);
        
        
    }
    
    
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D)g;
        Rectangle2D rect = new Rectangle2D.Double(1, 1, getWidth()-2,getHeight()-2);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(0, 0, 20, 20);
        g2.setColor(Color.YELLOW);
        g2.fillOval(0, 0, 20, 20);
        
        //g2.fillOval(0, 0, 20, 20);
    }
    
    public void SolaGit()
    {
        int PosX = getX();
        int PosY = getY();
                
        PosX-= mGenislik;
        this.setBounds(PosX,PosY,mGenislik,mGenislik);
    }
    public void SagaGit()
    {
        int PosX = getX();
        int PosY = getY();
                
        PosX+= mGenislik;
        this.setBounds(PosX,PosY,mGenislik,mGenislik);
    }
    public void YukariGit()
    {
        int PosX = getX();
        int PosY = getY();
                
        PosY-= mGenislik;
        this.setBounds(PosX,PosY,mGenislik,mGenislik);
    }
    public void AsagiGit()
    {
        int PosX = getX();
        int PosY = getY();
                
        PosY+= mGenislik;
        this.setBounds(PosX,PosY,mGenislik,mGenislik);
    }
    
    public Kutu KuyrukOlustur()
    {
        
        
        Kutu K = new Kutu();
        
        
        
        int X = getX();
        int Y = getY();
        
        K.setBounds(X,Y,mGenislik,mGenislik);
        K.mYon = -mYon;
        
        K.Hareket();
        
        K.mYon = mYon;
        
        return K;
    }
    
    public void Hareket()
    {
        if (mYon == Yon.SOL) {
            SolaGit();
        }
        else if (mYon == Yon.SAG) {
            SagaGit();
        }
        else if (mYon == Yon.ASAGI) {
            AsagiGit();
        }
        else{
            YukariGit();
        }
    }
    
    
    
}
