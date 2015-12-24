/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.CardLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author robert
 */
public class Dictionary extends JFrame {
    public static final String FILE_NAME = "src/data.csv", SEPARATOR = ";";    
    private static JFrame mainFrame;
    private final Map<String, Collection<Definition>> definitionMap; // phrase, definition
    private static final Map<String, JPanel> PANEL_MAP = new HashMap<>();
    private final JPanel mainPanel, editPanel;
    
    public Dictionary() {
        super("Słownik"); // set title
        setLayout(new CardLayout());
        definitionMap = new HashMap<>();
        mainPanel = new MainPanel(definitionMap);
        add (mainPanel);
        editPanel = new EditPanel(definitionMap);
        add (editPanel);
        
        PANEL_MAP.put("mainPanel", mainPanel);
        PANEL_MAP.put("editPanel", editPanel);        
        try {
            File f = new File(FILE_NAME);
            if (!f.exists() || f.isDirectory())
                initTestCSVfile();
            loadDataFromFile();
        } catch (IOException ex) {
        }
    }
    
    private void loadDataFromFile() throws IOException {
        BufferedReader fr = null; // file reader
        try {
            fr = new BufferedReader(new FileReader(FILE_NAME));
            String line = "";
            while ((line = fr.readLine()) != null) 
            {
                String[] values = line.split(SEPARATOR);
                if (values.length == 2) {
                    Definition def = new Definition(values[0], values[1]); // phrase, text
                    String phrase = values[0].toLowerCase().trim();
                    Collection<Definition> coll = null;
                    coll = definitionMap.get(phrase);
                    if (coll == null || coll.isEmpty()) {
                        List<Definition> list = new ArrayList<>();
                        list.add(def);
                        definitionMap.put(phrase, list);
                    } else {
                        coll.add(def);
                    }
                }
            }
            System.out.println("Loaded definitions from \"" + FILE_NAME + "\" file");
            printMap();
        } finally {
            if (fr != null)
                fr.close();
        }
    }
    
    private void initTestCSVfile() throws IOException {
        FileWriter fw = null;         
        try {
            fw = new FileWriter(new File(FILE_NAME));
            for (int i = 0; i < 3; i++) {
                String p = "Test " + (i + 1);
                String t = "this is sample text for " + p;
                fw.append(p + SEPARATOR + t + "\n");
            }
            fw.append("Test 1;qwertyuy\n");
            System.out.println("Sample \".csv\" file has beed initialized.");
        } finally {
            if (fw != null)
                fw.close();
        }
    }
    
    public static Dictionary getInstance() {
        return (Dictionary) mainFrame;
    }
    private void printMap() {
        for (Collection<Definition> coll : definitionMap.values()) 
            for (Definition d : coll)
                System.out.println(d.toString() + "\n");
        
    }
    public static void swapPanel (final String PANEL_NAME) {
        for (JPanel p : PANEL_MAP.values())
            p.setVisible(false);
        
        PANEL_MAP.get(PANEL_NAME).setVisible(true);
    }

    /**
     * @param args the command line arguments - no needed
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame = new Dictionary();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setResizable(false);
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
            }
        });
    }
    
}
