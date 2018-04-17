package Vista.SettingsViews;

import Vista.SwingModifications.IconTextField;

import java.awt.event.KeyEvent;

public class JNumberTextField extends IconTextField {

    public JNumberTextField(String iconName,String hint,String toolTip){
        super(iconName,hint,toolTip);
    }

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
