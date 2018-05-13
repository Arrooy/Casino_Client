package Vista.SettingsViews;


import Model.AssetManager;
import Model.WalletEvolutionMessage;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Top5View extends JPanel {

    private JTable table;
    private final static Color TRANSPARENT = new Color(0,0,0,0);
    private final static Color CREMA = new Color (218, 204, 164);
    private final static Color GRANA = new Color(125, 28, 37);
    private final static Color VERD = new Color(40, 73, 7);
    private final static Color GROC = new Color(237, 175, 67);
    private final static Color MARRO = new Color(56,37,19);
    private final static Color VERDFOSC = new Color(104,125,72, 255);

    private static final String[] columnNames = {"Game",
                                                "Money before",
                                                "Money after"};

    public Top5View(){
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel jPanelGridTaula = new JPanel(new GridBagLayout());
        jPanelGridTaula.setOpaque(false);
        jPanelGridTaula.setBorder(BorderFactory.createEmptyBorder());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        c.fill = GridBagConstraints.CENTER;

        Object [][] data = new Object[1][3];
        data[0][0] = "Loading";
        data[0][1] = "Loading";
        data[0][2] = "Loading";
        table = new JTable(data,columnNames);
        table.setGridColor(MARRO);
        table.setForeground(CREMA);
        table.setOpaque(false);
        ((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setOpaque(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(MARRO);
        header.setForeground(CREMA);
        header.setFont(AssetManager.getEFont(15));
        header.setBorder(BorderFactory.createLineBorder(MARRO));

        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);
        table.setEnabled(false);
        table.setPreferredScrollableViewportSize(new Dimension(400,400));
        table.setDefaultEditor(Object.class, null);

        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.setOpaque(false);
        jScrollPane.getViewport().setOpaque(false);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollBar jScrollBar = jScrollPane.getVerticalScrollBar();
        jScrollBar.setOpaque(false);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        jPanelGridTaula.add(jScrollPane, c);

        add(jPanelGridTaula,BorderLayout.CENTER);
    }

    public void updateWallet(WalletEvolutionMessage newWallet) {

        JTable aux;

        if(newWallet != null){

            int max = newWallet.getTransactions().size(),min = 3;
            Object [][] newData = new Object[max][min];
            int acumulador = 0;

            for(int i = 0; i < max; i++){
                if(i == 0){
                    newData[i][0] = "Initial gift";
                }else{
                    newData[i][0] = getTypeName(newWallet.getTransactions().get(i).getType());
                }
                newData[i][1] = acumulador;
                newData[i][2] = acumulador + newWallet.getTransactions().get(i).getGain();
                acumulador += newWallet.getTransactions().get(i).getGain();
            }
            aux = new JTable(newData, columnNames);
        }else{
            System.out.println("WALLET IS NULL");
            Object [][] data = new Object[1][3];
            data[0][0] = "Loading";
            data[0][1] = "Loading";
            data[0][2] = "Loading";
            aux = new JTable(data, columnNames);
        }

        table.setModel(aux.getModel());
        table.revalidate();
        updateUI();
    }

    private String getTypeName(int i){
        String nomType;
        switch (i){
            case 0:
                 nomType = "Deposit";
                break;
            case 1:
                nomType = "Horses";
                break;
            case 2:
                nomType = "Roulette";
                break;
            default:
                nomType = "BlackJack";
        }
        return nomType;
    }
}
