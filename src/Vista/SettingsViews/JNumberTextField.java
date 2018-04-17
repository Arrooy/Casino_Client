package Vista.SettingsViews;

import Vista.SwingModifications.IconTextField;

import java.awt.event.KeyEvent;

public class JNumberTextField extends IconTextField {

    public JNumberTextField(String iconName,String hint,String toolTip){
        super(iconName,hint,toolTip);
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        System.out.println(e.getKeyChar() + " " + e.getKeyCode());
        if(Character.isDigit(e.getKeyChar()) || e.getKeyChar() < 32 || e.getKeyChar() == 127)
            super.processKeyEvent(e);
        else
            e.consume();
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
