package Vista.SwingModifications;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Helper dels dos nous elements de Swing IconTextField i IconPasswordField.
 * Crea els borders del textFields i afegeix l'icono a la posicio corresponent
 */
public class IconTextComponentHelper {

    /** Espaiat del Icono respecte l'inici del TextField*/
    private static final int ICON_SPACING = 3;

    /** Border del TextField actual*/
    private Border mBorder;

    /** Icono que s'ha d'afegir*/
    private Icon mIcon;

    /** Border del TextField original*/
    private Border mOrigBorder;

    /** JTextComponent que es tracta, pot ser JPasswordField o JTextField*/
    private JTextComponent mTextComponent;

    /**
     * Crea un nou helper referenciat al JTextComponent anomenat component
     * @param component component que necesita el helper
     */
    public IconTextComponentHelper(JTextComponent component) {
        mTextComponent = component;
        mOrigBorder = component.getBorder();
        mBorder = mOrigBorder;
    }

    /**
     * Si l'icono ha estat definit, aquest es pinta al interior del JTextComponent
     * @param g graphics del JTextComponent al que esta ajudant el helper
     */
    public void onPaintComponent(Graphics g) {
        if (mIcon != null) {
            Insets iconInsets = mOrigBorder.getBorderInsets(mTextComponent);
            mIcon.paintIcon(mTextComponent, g, iconInsets.left, iconInsets.top);
        }
    }

    /**
     * En el moment que es defineix el border del JTextComponent amb el que s'esta treballant,
     * si sha definit un icono, es crea un borde nou i es guarda a mBorder
     * @param border border del JTextComponent amb el que s'esta treballant
     */
    public void onSetBorder(Border border) {
        mOrigBorder = border;

        if (mIcon == null) {
            mBorder = border;
        } else {
            Border margin = BorderFactory.createEmptyBorder(0, mIcon.getIconWidth() + ICON_SPACING, 0, 0);
            mBorder = BorderFactory.createCompoundBorder(border, margin);
        }
    }

    /**
     * Defineix un icono per al JTextComponent
     * @param icon icono que s'ha dafegir al JTextComponent
     */
    public void onSetIcon(Icon icon) {
        mIcon = icon;
        resetBorder();
    }


    //Reinicia el border del JTextComponent
    private void resetBorder() {
        mTextComponent.setBorder(mOrigBorder);
    }

    /**
     * Retorna el border actual del JTextComponent
     * @return border que te actualment el JTextComponent
     */
    public Border getBorder() {
        return mBorder;
    }

}