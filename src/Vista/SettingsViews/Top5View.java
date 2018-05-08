package Vista.SettingsViews;


import Model.WalletEvolutionMessage;

import javax.swing.*;
import java.awt.*;

public class Top5View extends JPanel {

    private JTable table;
    private static final String[] columnNames = {"Game",
            "Money before",
            "Money after"};

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

        JTable aux = new JTable(newData, columnNames);
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
