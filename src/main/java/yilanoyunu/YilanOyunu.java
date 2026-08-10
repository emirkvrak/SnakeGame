
package yilanoyunu;

import javax.swing.SwingUtilities;


public class YilanOyunu 
{

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new yilanoyunu.ui.OyunPenceresi();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
    
}
