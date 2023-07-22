
package yilanoyunu;


import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;


public class AnaEkran 
{
    private int mGenislik = 800;
    private int mYukseklik = 800;
    
    private static AnaEkran mPencere = null;
    
    JFrame frame = new JFrame();
    JButton buton = new JButton();
    private Image resim;
    public AnaEkran() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        
        File file = new File("resim/yılanmüzik.wav");
        AudioInputStream audiostream = AudioSystem.getAudioInputStream(file);
        Clip clip1 = AudioSystem.getClip();
        clip1.open(audiostream);
        clip1.start();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(700, 300, 600, 600);
        
        
        buton = new JButton();
        ImageIcon img = new ImageIcon("resim/orman2.png");
        resim = img.getImage();
        buton.setIcon(new ImageIcon(resim));
        frame.add(buton);
        
        buton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                Yılan yılan = new Yılan();
                frame.add(yılan);
                buton.setVisible(false);
                
            }
        });
        
        
        
        
        
    
    }
}
