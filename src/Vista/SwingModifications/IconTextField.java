package Vista.SwingModifications;

import Utils.AssetManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/** Modificacio del JTextField per a millorar la seva aparença*/
public class IconTextField extends JTextField {

    /** Amplada del icono que apareix en el JTextField*/
    private static final int ICON_WIDTH = 15;

    /** Altura del icono que apareix en el JTextField*/
    private static final int ICON_HEIGHT = 15;

    /** Helper per afegir l'icono i pintar el nou JTextField*/
    private IconTextComponentHelper mHelper = new IconTextComponentHelper(this);

    /** Indica si s'ha de mostrar la hint del JTextField*/
    private boolean showHint;

    /** El text de hint que s'ha de mostrar*/
    private String hintText;

    /**
     * Crea un IconTextField amb un icono, una hint i una tooltip
     * @param photoName nom de l'imatge que es vol fer servir d'icono
     * @param hint hint de la textbox
     * @param toolTip text que apareix al posar el mouse a sobre de la PasswordField
     */
    public IconTextField(String photoName,String hint,String toolTip) {
        super();
        showHint = true;
        hintText = hint;
        setToolTipText(toolTip);
        getHelper().onSetIcon((new ImageIcon(AssetManager.getImage(photoName,ICON_WIDTH,ICON_HEIGHT))));
    }

    /** Es pinta el component afegint la hint si es necesari*/
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        getHelper().onPaintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        if(showHint){
            FontMetrics metrics = g.getFontMetrics();
            g2d.setColor(new Color(150,150,150));
            g2d.drawString(hintText,getInsets().left,metrics.getMaxAscent() + 1);
        }
    }

    /** El helper pinta els borders adients*/
    @Override
    public void setBorder(Border border) {
        getHelper().onSetBorder(border);
        super.setBorder(getHelper().getBorder());
    }

    //Getter i Setters
    public void setHint(boolean showHint){
        this.showHint = showHint;
        repaint();
    }

    private IconTextComponentHelper getHelper() {
        if (mHelper == null)
            mHelper = new IconTextComponentHelper(this);
        return mHelper;
    }
}