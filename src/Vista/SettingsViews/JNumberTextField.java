package Vista.SettingsViews;

import Vista.SwingModifications.IconTextField;

import java.awt.event.KeyEvent;

/** Modificacio del IconTextField per a nomes acceptar nombres del 0-9 com a input*/
public class JNumberTextField extends IconTextField {

    /**
     * Crea una nou JNumberTextField definint el seu icono, la seva hint i el tooltip
     * @param iconName Nom de l'icono a pintar
     * @param hint hint de la JNumberTextField
     * @param toolTip Missatge que apareix al moure el mouse per sobre del Field
     */
    public JNumberTextField(String iconName,String hint,String toolTip){
        super(iconName,hint,toolTip);
    }

    /** Es fa override del ProcessKeyEvent per a eliminar totes les tecles que no siguin nombres del 0-9*/
    @Override
    protected void processKeyEvent(KeyEvent e) {

        //En cas d'introduir algo que no sigui un nombre, aquest caracter es consumeix
        if(Character.isDigit(e.getKeyChar()) || e.getKeyChar() < 32 || e.getKeyChar() == 127)
            super.processKeyEvent(e);
        else
            e.consume();
    }

    public long getNumber() {
        long result = -1;
        String text = getText();
        if (text != null && !"".equals(text)) {
            result = Long.valueOf(text);
        }
        return result;
    }

}
