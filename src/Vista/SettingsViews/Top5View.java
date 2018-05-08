package Vista.SettingsViews;


import Model.WalletEvolutionMessage;

import javax.swing.*;
import java.awt.*;

public class Top5View extends JPanel {

    private JTable table;
    private static final String[] columnNames = {"Game",
            "Money before",
            "Money after"};

    private Object[][] data;

    public Top5View(){

        this.setLayout(new BorderLayout());

        int max = 5,min = 3;
        Object [][] newData = new Object[max][min];
        for(int i = 0; i < max; i++){
            for(int j = 0; j < min; j++){
                newData[i][j] = "" + i + j;
            }
        }

        table = new JTable(newData,columnNames);
        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);
        table.setPreferredScrollableViewportSize(new Dimension(400,150));
        table.setDefaultEditor(Object.class, null);

        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(jScrollPane,BorderLayout.CENTER);
    }

    public void updateWallet(WalletEvolutionMessage newWallet) {
        System.out.println("NEW WALLET RECIEVED");
        System.out.println(newWallet.getTransactions());

        int max = 5,min = 3;
        Object [][] newData = new Object[max][min];
        for(int i = 0; i < max; i++){
            for(int j = 0; j < min; j++){
                newData[i][j] = "klk" + i + j;
            }
        }

        JTable aux = new JTable(newData, columnNames);
        table.setModel(aux.getModel());
        table.revalidate();
        updateUI();
    }
}
