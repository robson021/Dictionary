package dictionary;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author robert
 */
public class NewRecordPanel extends AbstractPanel {

    public NewRecordPanel(Map<String, Collection<Definition>> m) {
        super(m, "Panel dodawania.");
    }
   

    @Override
    protected void confirmButtonAction() {
                    //TODO
        try {
            Dictionary.updateFile();
            Dictionary.swapPanel("mainPanel");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
