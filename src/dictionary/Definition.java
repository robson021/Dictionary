/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

/**
 *
 * @author robert
 */
public class Definition {
    private String phrase;
    private String text;
    
    public Definition (String p, String t) {
        text = t; phrase = p;
    }
    
    public String getPhrase() {
        return phrase;
    }
    public String getDefinitionText() {
        return text;
    }
    public void setNewDefinitionText(String t) {
        text = t;
    }
    public void setNewPhrase(String p) {
        phrase = p;
    } 
    
    @Override
    public String toString() {
        return phrase + Dictionary.SEPARATOR + text;
    }
}
