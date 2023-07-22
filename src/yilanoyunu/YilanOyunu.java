
package yilanoyunu;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;



public class YilanOyunu 
{

    
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException 
    {
        AnaEkran anaekran = new AnaEkran();
               
        anaekran.frame.setVisible(true);
       
        
       
    }
    
}
