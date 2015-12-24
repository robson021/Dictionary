
package dictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author robert
 */
public class EditPanel extends AbstractPanel {    
    private static String oldKey = null;
    private static int index;
    private final MainPanel mainPanel;
    
    public EditPanel(Map<String, Collection<Definition>> m, MainPanel mp) {
        super(m, "Panel edytowania.");
        mainPanel = mp;
    }
    
    /**
     * Edits existing record in the Dictionary. Also updates the data file on HDD.
     */
    @Override
    protected void confirmButtonAction() {
        String newPhrase = textField.getText().trim();
        String newText = textArea.getText();        
        if (newPhrase.isEmpty() || newText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Uzupełnij wszystkie pola!");
            return;
        }
        
        if (newText.contains(Dictionary.SEPARATOR)) {
            newText = newText.replace(Dictionary.SEPARATOR, " |");
            JOptionPane.showMessageDialog(this,
                    "Znak ';' jest niedozwolony. Wszystkie zostały zastąpione przez '|'");
        System.out.println(newText);
        }
        
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
