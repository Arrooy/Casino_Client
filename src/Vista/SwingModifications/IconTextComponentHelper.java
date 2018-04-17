package Vista.SwingModifications;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class IconTextComponentHelper {
    private static final int ICON_SPACING = 3;

    private Border mBorder;
    private Icon mIcon;
    private Border mOrigBorder;
    private JTextComponent mTextComponent;


    IconTextComponentHelper(JTextComponent component) {
        mTextComponent = component;
        mOrigBorder = component.getBorder();
        mBorder = mOrigBorder;
    }

    Border getBorder() {
        return mBorder;
    }

    void onPaintComponent(Graphics g) {
        if (mIcon != null) {
            Insets iconInsets = mOrigBorder.getBorderInsets(mTextComponent);
            mIcon.paintIcon(mTextComponent, g, iconInsets.left, iconInsets.top);
        }
    }

    void onSetBorder(Border border) {
        mOrigBorder = border;

        if (mIcon == null) {
            mBorder = border;
        } else {
            Border margin = BorderFactory.createEmptyBorder(0, mIcon.getIconWidth() + ICON_SPACING, 0, 0);
            mBorder = BorderFactory.createCompoundBorder(border, margin);
        }
    }

    void onSetIcon(Icon icon) {
        mIcon = icon;
        resetBorder();
    }

    private void resetBorder() {
        mTextComponent.setBorder(mOrigBorder);
    }
}