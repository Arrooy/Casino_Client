package Vista.SwingModifications;

import Utils.AssetManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/** Modificacio del JPasswordField per a millorar la seva aparença*/
public class IconPasswordField extends JPasswordField {

    /** Amplada del icono que apareix en el JPasswordField*/
    private static final int ICON_WIDTH = 15;

    /** Altura del icono que apareix en el JPasswordField*/
    private static final int ICON_HEIGHT = 15;

    /** Helper per afegir l'icono i pintar el nou JPasswordField*/
    private IconTextComponentHelper mHelper = new IconTextComponentHelper(this);

    /** Indica si s'ha de mostrar la hint de la passwordField*/
    private boolean showHint;

    /** El text de hint que s'ha de mostrar*/
    private String hintText;

    /**
     * Crea un IconPasswordField amb un icono, una hint i una tooltip
     * @param photoName nom de l'imatge que es vol fer servir d'icono
     * @param hint hint de la textbox
     * @param toolTip text que apareix al posar el mouse a sobre de la PasswordField
     */
    public IconPasswordField(String photoName,String hint,String toolTip) {
        this(photoName,hint,0,toolTip);
    }

    /**
     * Crea un IconPasswordField amb un icono, una hint, una tooltip i amb el nombre de columnes que ocupa
     * @param photoName nom de l'imatge que es vol fer servir d'icono
     * @param hint hint de la textbox
     * @param toolTip text que apareix al posar el mouse assobre de la PasswordField
     * @param columns nombre de columnes que ocupa el IconPasswordField
     */
    public IconPasswordField(String photoName,String hint,int columns,String toolTip) {
        super(columns);
        showHint = true;
        hintText = hint;
        setToolTipText(toolTip);
        getHelper().onSetIcon(new ImageIcon(AssetManager.getImage(photoName,ICON_WIDTH,ICON_HEIGHT)));
    }

    /** Es pinta el component afegint la hint si es necesari*/
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        getHelper().onPaintComponent(g);
        if(showHint){
            FontMetrics metrics = g.getFontMetrics();
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
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
    public void setHint(boolean hint) {
        this.showHint = hint;
        repaint();
    }

    private IconTextComponentHelper getHelper() {
        if (mHelper == null)
            mHelper = new IconTextComponentHelper(this);

        return mHelper;
    }
}