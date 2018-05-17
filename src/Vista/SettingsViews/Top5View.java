package Vista.SettingsViews;


import Model.AssetManager;
import Model.WalletEvolutionMessage;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**Classe que crea el panell que visualitza amb una taula la fluctuacio de diners de l'usuari*/
public class Top5View extends JPanel {

    /**Taula amb la informacio monetaria*/
    private JTable table;

    /**Color Crema*/
    private final static Color CREMA = new Color (218, 204, 164);

    /**Color Marro*/
    private final static Color MARRO = new Color(56,37,19);

    /**Array de cadenes amb el nom de les capçaleres de la taula*/
    private static final String[] columnNames = {"Game",
                                                "Money before",
                                                "Money after"};

    /**Constructor del panell que defineix on i com es col·loquen els elements*/
    public Top5View(){
        //Es defineix el Layout
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        //Panell per centrar la taula al centre del panell
        JPanel jPanelGridTaula = new JPanel(new GridBagLayout());
        jPanelGridTaula.setOpaque(false);
        jPanelGridTaula.setBorder(BorderFactory.createEmptyBorder());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        c.fill = GridBagConstraints.CENTER;

        //Contingut de la fila
        Object [][] data = new Object[1][3];
        data[0][0] = "Loading";
        data[0][1] = "Loading";
        data[0][2] = "Loading";

        //Creacio de la taula
        table = new JTable(data,columnNames);

        //Canvi de colors de la taula per a que s'ajusti al fons
        table.setGridColor(MARRO);
        table.setForeground(CREMA);
        table.setOpaque(false);
        ((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setOpaque(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(MARRO);
        header.setForeground(CREMA);
        header.setFont(AssetManager.getEFont(15));
        header.setBorder(BorderFactory.createLineBorder(MARRO));

        //Es desactiven les funcionalitats de la taula perque nomes s'utilitza per visualitzar la informacio
        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);
        table.setEnabled(false);

        //Mida de la taula
        table.setPreferredScrollableViewportSize(new Dimension(340,400)); // 400 400
        table.setDefaultEditor(Object.class, null);

        //Panell amb scroll per poder veure tota la informacio de la taula en cas de que n'hi hagi
        //mes de la que es pot visualitzar amb la mida que s'ha definit la taula.
        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.setOpaque(false);
        jScrollPane.getViewport().setOpaque(false);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollBar jScrollBar = jScrollPane.getVerticalScrollBar();
        jScrollBar.setOpaque(false);

        jScrollPane.setBorder(BorderFactory.createEmptyBorder());

        jPanelGridTaula.add(jScrollPane, c);

        //Es col·loca la taula al centre del panell
        add(jPanelGridTaula,BorderLayout.CENTER);
    }

    /**Metode que actualitza la taula segons les diferents fluctuacions de diners que ha patit l'usuari*/
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

    /**Metode que indica des d'on s'ha produit la fluctuacio de diners
     * @return String amb el nom d'on s'ha produit la fluctuacio*/
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
