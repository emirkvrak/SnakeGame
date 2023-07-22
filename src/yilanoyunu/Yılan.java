
package yilanoyunu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;


public class Yılan extends JLabel
{
    public Kutu mHead = new Kutu();
    
    public Timer mTimer = null;
    public Timer mTimer2 = null;
    
    public Yem mYem = new Yem();
    
    public Mayin mMayin = new Mayin();
    
    public Random mRandom = null;
    
    public ArrayList<Kutu> Liste = new ArrayList<Kutu>();
    
    
    
    public Yılan()
    {
        mRandom = new Random(System.currentTimeMillis());
        
        this.addKeyListener(new KlavyeKontrol());
        this.setFocusable(true);
        
        mTimer = new Timer(100, new TimerKontrol());
        mTimer.start();
        
        mTimer2 = new Timer(2000, new TimerKontrol2());
        mTimer2.start();
        
        
        
        
        Liste.add(mHead);
        
        this.add(mYem);
        this.add(mMayin);
        this.add(mHead);
        
    }
    
    public void KuyrukEkle()
    {
        Kutu K = Liste.get(Liste.size()-1).KuyrukOlustur();
            
        Liste.add(K);
        this.add(K);
    }
    
    public void YemEkle()
    {
        int Width = getWidth()-30-mYem.mGenislik;
        int Height = getHeight()-30-mYem.mGenislik;
        
        int PosX = 10+Math.abs(mRandom.nextInt())%Width;
        int PosY = 10+Math.abs(mRandom.nextInt())%Height;
        
        
        PosX = PosX - PosX%20;
        PosY = PosY - PosY%20;
        
        for (int i = 0; i < Liste.size(); i++) 
        {
            if ((PosX == Liste.get(i).getX())&&(PosY==Liste.get(i).getY())) 
            {
                YemEkle();
                return;
            }
        }
        
        mYem.setPosition(PosX, PosY);
    }
    public void MayinEkle()
    {
        int Width = getWidth()-30-mMayin.mGenislik;
        int Height = getHeight()-30-mMayin.mGenislik;
        
        int PosX = 10+Math.abs(mRandom.nextInt())%Width;
        int PosY = 10+Math.abs(mRandom.nextInt())%Height;
        
        
        
        PosX = PosX - PosX%20;
        PosY = PosY - PosY%20;
        
        
        
        mMayin.setPosition(PosX, PosY);
    }
    
    public void HepsiniYurut()
    {
        for (int i = Liste.size()-1; i>0 ;i--) {
            
            Kutu Onceki = Liste.get(i-1);
            Kutu Sonraki = Liste.get(i);
            
            
            
            Liste.get(i).Hareket();
            Sonraki.mYon = Onceki.mYon;
        }
        mHead.Hareket();
    }
    
    public boolean  CarpısmaVarmi()
    {
        
        
        
        int genislik = getHeight();
        int yukseklik = getWidth();
        
        if (mHead.getX()<=-1) 
            return true;
        if (mHead.getX()+mHead.mGenislik>=genislik+20) 
            return true;
        if (mHead.getY()<=-1) 
            return true;
        if (mHead.getY()+mHead.mGenislik>=yukseklik-20) 
            return true;
        
        
        for (int i = 1; i < Liste.size(); i++) {
            int X = Liste.get(i).getX();
            int Y = Liste.get(i).getY();
            
            if ((X == mHead.getX())&&(Y == mHead.getY()))
                return true;
        }
        
        if (mYem.getX()==mHead.getX()&&(mYem.getY()==mHead.getY())) 
        {
            KuyrukEkle();
            YemEkle();
            
            
        }
        if (mMayin.getX()==mHead.getX()&&(mMayin.getY()==mHead.getY())) 
            {
                return true;
                
            }

        
        return false;
    }
    
    
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D)g;
        Rectangle2D rect = new Rectangle2D.Double(0, 0, getWidth()-3,getHeight());
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(5));
        g2.draw(rect);
    }
    
    class KlavyeKontrol implements KeyListener
    {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) 
        {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (mHead.mYon!=Yon.SAG) {
                    mHead.mYon = Yon.SOL;
                }
                
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (mHead.mYon!=Yon.SOL) {
                    mHead.mYon = Yon.SAG;
                }
                
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (mHead.mYon!=Yon.ASAGI) {
                    mHead.mYon = Yon.YUKARI;
                }
                
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (mHead.mYon!=Yon.YUKARI) {
                    mHead.mYon = Yon.ASAGI;
                }
                
            }
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
        
    }
    
    class TimerKontrol implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            HepsiniYurut();
            if (CarpısmaVarmi()) {
                mTimer.stop();
            }
            
        }
    
    }
    class TimerKontrol2 implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (!CarpısmaVarmi()) {
                MayinEkle();
            }
            
            if (CarpısmaVarmi()) {
                mTimer2.stop();
                
            }
            
        }
    
    }
    
    @Override
    protected void paintComponent(Graphics g)//JPanel'in arkaplanına resim ekliyor
    {
        super.paintComponent(g);
    
        int baslangicX = 0;
        int baslangicY = 0;
    
        int bitisX = getSize().width;
        int bitisY = getSize().height;
        
        Image image = new ImageIcon("resim/cimen.png").getImage();
        
        g.drawImage(image, baslangicX, baslangicY, bitisX, bitisY, null);
    }
}
