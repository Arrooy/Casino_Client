package Vista.SwingModifications;

import Utils.AssetManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class IconTextField extends JTextField {

    private static final int ICON_WIDTH = 15;
    private static final int ICON_HEIGHT = 15;
    private boolean showHint;
    private String hintText;


    private IconTextComponentHelper mHelper = new IconTextComponentHelper(this);

    public IconTextField(String photoName,String hint,String toolTip) {
        super();
        showHint = true;
        hintText = hint;
        setToolTipText(toolTip);
        setIcon(new ImageIcon(AssetManager.getImage(photoName,ICON_WIDTH,ICON_HEIGHT)));
    }

    public void setHint(boolean showHint){
        this.showHint = showHint;
        repaint();
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
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        if(showHint){
            FontMetrics metrics = g.getFontMetrics();
            g2d.setColor(new Color(150,150,150));
            g2d.drawString(hintText,getInsets().left,metrics.getMaxAscent() + 1);
        }
    }

    public void setIcon(Icon icon) {
        getHelper().onSetIcon(icon);
    }

    @Override
    public void setBorder(Border border) {
        getHelper().onSetBorder(border);
        super.setBorder(getHelper().getBorder());
    }
}