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

        JPanel jPanelGridTaula = new JPanel(new GridBagLayout());
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
        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);
        table.setEnabled(false);
        table.setPreferredScrollableViewportSize(new Dimension(400,400));
        table.setDefaultEditor(Object.class, null);

        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
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
