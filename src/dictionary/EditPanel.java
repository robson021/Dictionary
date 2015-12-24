/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author robert
 */
public class EditPanel extends JPanel {
    private final Map<String, Collection<Definition>> map;
    private final JButton backButton, confirmButton;
    private final JTextField textField;
    private final JTextArea textArea;
    private static String oldKey = null;
    private static int index;
    private final MainPanel mainPanel;
    
    public EditPanel(Map<String, Collection<Definition>> m, MainPanel mp) {
        super(new BorderLayout()); // set layout
        map = m;
        mainPanel = mp;
        JPanel southPanel = new JPanel(); // footer
        southPanel.add(new JLabel("Panel edytowania."));
        add (southPanel, BorderLayout.SOUTH);
        
        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.add (new JLabel("Nowa fraza wyszukiwania: "));
        textField = new JTextField (MainPanel.DEFAULT_NUM_COLUMNS);
        northPanel.add(textField);
        add (northPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;    
        textArea = new JTextArea("Nowa definicja...", 
                MainPanel.DEFAULT_NUM_COLUMNS, MainPanel.DEFAULT_NUM_COLUMNS*2);
        centerPanel.add(new JScrollPane(textArea), gbc);
        
        JPanel innerButtonPanel = new JPanel(new FlowLayout());
        confirmButton = new JButton("Zatwierdź");
        confirmButton.addActionListener((ActionEvent e) -> {
            editData();
        });
        backButton = new JButton("Powrót");
        backButton.addActionListener((ActionEvent e) -> {
            Dictionary.swapPanel("mainPanel");
        });
        
        innerButtonPanel.add (confirmButton);
        innerButtonPanel.add (backButton);
        
        gbc.gridy++;
        centerPanel.add(innerButtonPanel, gbc);
        
        add (centerPanel, BorderLayout.CENTER);        
    }
    
    private void editData() {
        String newPhrase = textField.getText().trim();
        String newText = textArea.getText();        
        if (newPhrase.isEmpty() || newText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Uzupełnij wszystkie pola!");
            return;
        }
        
        String replace = newText.replace(Dictionary.SEPARATOR, " |");
        newText = replace;
        if (newText.contains(Dictionary.SEPARATOR)) 
            JOptionPane.showMessageDialog(this,
                    "Znak ';' jest niedozwolony. Wszystkie zostały zastąpione przez '|'");
        //System.out.println(newText);
        
        List<Definition> items = (List<Definition>) map.get(oldKey);
        
        if (!newPhrase.toLowerCase().equals(oldKey)) { // new unique key
            Collection<Definition> coll = null;
            coll = map.get(newPhrase.toLowerCase());
            if (coll != null && !coll.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ten klucz jest już używany!");
                return;                
            }
            items.remove(index);   
            List<Definition> tmpList = new ArrayList<>();
            tmpList.add(new Definition(newPhrase, newText));
            map.put(newPhrase.toLowerCase(), tmpList);
            if (items.isEmpty()) 
                map.remove(oldKey);
            
        } else { // add to old key collection
            items.set(index, new Definition(newPhrase.toLowerCase(), newText));
        }
        try {
            Dictionary.updateFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        textField.setText(""); textArea.setText("");
        mainPanel.clearFields();
        Dictionary.swapPanel("mainPanel");
    }
    
    static void setOldKey (String k, int i) {
        oldKey = k; index = i;
    }
}
