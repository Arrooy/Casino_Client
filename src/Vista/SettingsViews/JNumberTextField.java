package Vista.SettingsViews;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;

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
        public long getNumber() {
            long result = -1;
            String text = getText();
            if (text != null && !"".equals(text)) {
                result = Long.valueOf(text);
            }
            return result;
        }

}
