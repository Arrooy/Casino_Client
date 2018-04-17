package Vista.SwingModifications;

import Model.AssetManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class IconPasswordField extends JPasswordField {

    private static final int ICON_WIDTH = 15;
    private static final int ICON_HEIGHT = 15;

    private IconTextComponentHelper mHelper = new IconTextComponentHelper(this);
    private boolean showHint;
    private String hintText;

    public IconPasswordField(String photoName,String hint,String toolTip) {
        super();
        setToolTipText(toolTip);
        showHint = true;
        hintText = hint;
        setIcon(photoName);
    }
    public IconPasswordField(String photoName,String hint,int columns,String toolTip) {
        super(columns);
        showHint = true;
        hintText = hint;
        setToolTipText(toolTip);
        setIcon(photoName);
    }

    private IconTextComponentHelper getHelper() {
        if (mHelper == null)
            mHelper = new IconTextComponentHelper(this);

        return mHelper;
    }

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

    public void setIcon(String nameIcon) {
        getHelper().onSetIcon(new ImageIcon(AssetManager.getImage(nameIcon,ICON_WIDTH,ICON_HEIGHT)));
    }

    @Override
    public void setBorder(Border border) {
        getHelper().onSetBorder(border);
        super.setBorder(getHelper().getBorder());
    }

    public void setHint(boolean hint) {
        this.showHint = hint;
        repaint();
    }
}