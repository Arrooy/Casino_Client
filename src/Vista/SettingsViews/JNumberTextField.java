package Vista.SettingsViews;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class JNumberTextField extends JTextField {
    @Override
    protected void processKeyEvent(KeyEvent e) {
        if (Character.isDigit(e.getKeyChar())) {
            super.processKeyEvent(e);
        }
        e.consume();
        return;
    }
    /** En cas d'introduir algo que no sigui un nombre, aquest caracter es consumeix*/
        public int getNumber() {
            int result = -1;
            String text = getText();
            if (text != null && !"".equals(text)) {
                result = Integer.valueOf(text);
            }
            return result;
        }

}
