/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.jwinzip;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import vavi.swing.binding.table.TableModel;
import vavi.util.Debug;
import vavi.util.RegexFileFilter;
import vavi.util.archive.Archive;
import vavi.util.archive.Archives;
import vavi.util.archive.Entry;


/**
 * Java version of winzip.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 021103 nsano initial version <br>
 *          0.01 021222 nsano refine <br>
 *          0.02 030112 nsano add view function <br>
 *          0.03 030210 nsano avoid File#exists() bug ??? <br>
 *          0.04 030211 nsano fix 0.03, about directory entry <br>
 */
public class JWinZip {

    /** */
    private static final ResourceBundle rb = ResourceBundle.getBundle("JWinZipResources", Locale.getDefault());

    /** */
    private Archive archive;

    /**
     * @param entry archive entry
     * @param file file to output
     */
    private void extract(Entry entry, Path file) throws IOException {
        try (InputStream is = new BufferedInputStream(archive.getInputStream(entry))) {
            Files.copy(is, file);
            Files.setLastModifiedTime(file, FileTime.fromMillis(entry.getTime()));
        }
    }

    /** */
    private final JTable table;

    /** */
    private final JPopupMenu popupMenu;

    /** */
    private final JFrame frame;

    /**
     * GUI
     */
    private JWinZip(String[] args) throws IOException {
        frame = new JFrame();

        popupMenu = getPopupMenu();

        table = new JTable();
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setFont(new Font(rb.getString("panel.jWinZip.font.name"), Font.PLAIN, 12));
        table.addMouseListener(mouseListener);
        table.getTableHeader().setReorderingAllowed(false);

        archive = Archives.getArchive(new File(args[0]));

        TableModel<Entry> model = new TableModel<>(new ArchiveModel(archive));
        model.bind(table);

        JScrollPane sp = new JScrollPane(table);

        frame.setJMenuBar(getMenuBar());
        frame.getContentPane().add(getToolBar(), BorderLayout.NORTH);
        frame.getContentPane().add(sp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(640, 480));
        frame.setTitle("JWinZip");
        frame.pack();
        frame.setVisible(true);

        init();
    }

    /**
     * Creates the menu bar．
     */
    JMenuBar getMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem menuItem;

        // file
        JMenu menu = new JMenu(rb.getString("menu.file"));
        menu.setMnemonic(KeyEvent.VK_F);

        menuItem = menu.add(extractAction);
        menuItem.setMnemonic(KeyEvent.VK_X);
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = menu.add(exitAction);
        menuItem.setMnemonic(KeyEvent.VK_C);
        menu.add(menuItem);

        menuBar.add(menu);

        // help
        menu = new JMenu(rb.getString("menu.help"));
        menuItem = menu.add(aboutAction);
        menuItem.setMnemonic(KeyEvent.VK_A);
        menu.add(menuItem);

        menuBar.add(menu);

        return menuBar;
    }

    /**
     * Creates the tool bar．
     */
    JToolBar getToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        JButton button;

        button = toolBar.add(extractAction);
        // button.setBorderPainted(false);
        // button.setFocusPainted(true);
        button.setToolTipText(button.getText());

        ToolTipManager.sharedInstance().registerComponent(toolBar);

        return toolBar;
    }

    /**
     * Creates the popup menu.．
     */
    JPopupMenu getPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        popupMenu.add(viewAction);

        return popupMenu;
    }

    /** */
    private static void makeSureParentDirs(Path file) throws IOException {
        Path parent = file.getParent();
// Debug.println("file: " + file);
// Debug.println("parent: " + parent.isDirectory() + ": " + parent.exists() + ": " + parent);
        if (!Files.exists(parent)) {
            Files.createDirectories(parent);
Debug.println("create dir: " + parent);
        }
    }

    /**
     * TODO duplication
     * TODO action only for selected files
     */
    private void extractAll(Path dir) throws IOException {

        Entry[] entries = archive.entries();
Debug.println("dir: " + dir);
        for (Entry entry : entries) {

            Path file = dir.resolve(entry.getName());

            if (entry.isDirectory()) {
                if (!Files.exists(file)) {
                    Files.createDirectories(file);
Debug.println("create dir: " + file);
                }
            } else {
                file = dir.resolve(entry.getName());
                makeSureParentDirs(file);

                try {
                    extract(entry, file);
                    System.err.println("Melting " + entry.getName() + " to " + file);
                } catch (IOException e) {
                    System.err.println("Melting to " + file + " failed.");
                    // e.printStackTrace(System.err);
                }
            }
        }
    }

    /** */
    private void view() throws IOException {
        Entry entry = archive.entries()[table.getSelectedRow()];

        Path file = Files.createTempFile("JWinZip", "tmp");
        makeSureParentDirs(file);

        extract(entry, file);
        System.err.println("Temporary " + entry.getName() + " to " + file);

        Runtime.getRuntime().exec(new String[] {"notepad", file.toString()});
    }

    /** */
    Properties appProps = new Properties();

    /** */
    private static final Path APP_PROPS = Path.of(System.getProperty("user.home"), ".config", "jwinzip", "config");

    /** */
    private void init() throws IOException {
Debug.println(APP_PROPS);
        InputStream is;
        try {
            is = Files.newInputStream(APP_PROPS);
        } catch (NoSuchFileException e) {
            if (!Files.exists(APP_PROPS.getParent())) {
                Files.createDirectories(APP_PROPS.getParent());
            }
            Files.createFile(APP_PROPS);
            is = Files.newInputStream(APP_PROPS);
        }
        appProps.load(is);
        is.close();
    }

    /** */
    private void exit() throws IOException {
        OutputStream os = Files.newOutputStream(APP_PROPS);
        appProps.store(os, "JWinZip");
        os.close();
    }

    // ----

    /** */
    private Action extractAction = new AbstractAction(rb.getString("action.extract"),
                                                      (ImageIcon) UIManager.get("jWinZip.extractIcon")) {
        private JFileChooser fc = new JFileChooser();

        void init() {
            File cwd;
            if (appProps.getProperty("dir.extract") == null) {
                cwd = new File(System.getProperty("user.home"));
            } else {
                cwd = new File(appProps.getProperty("dir.extract"));
            }
            fc.setCurrentDirectory(cwd);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }

        public void actionPerformed(ActionEvent ev) {
            try {
                init();
                if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                Path dir = fc.getSelectedFile().toPath();
                appProps.setProperty("dir.extract", dir.toString());
                extractAll(dir);
            } catch (IOException e) {
Debug.printStackTrace(e);
            }
        }
    };

    /** "About" action */
    private Action aboutAction = new AbstractAction(rb.getString("action.about")) {
        public void actionPerformed(ActionEvent ev) {
            JOptionPane.showMessageDialog(null, "0.02", rb.getString("action.about"), JOptionPane.INFORMATION_MESSAGE);
        }
    };

    /** "Exit" action */
    private Action exitAction = new AbstractAction(rb.getString("action.exit")) {
        public void actionPerformed(ActionEvent ev) {
            try {
                exit();
            } catch (IOException e) {
Debug.printStackTrace(e);
            }
            frame.setVisible(false);
        }
    };

    /** "View" action */
    private Action viewAction = new AbstractAction(rb.getString("action.view")) {
        public void actionPerformed(ActionEvent ev) {
            try {
                view();
            } catch (IOException e) {
Debug.printStackTrace(e);
            }
        }
    };

    /** */
    private MouseInputListener mouseListener = new MouseInputAdapter() {
        public void mouseClicked(MouseEvent ev) {
            if (SwingUtilities.isRightMouseButton(ev)) {
                if (table.getSelectedRow() != -1) {
                    int x = ev.getX();
                    int y = ev.getY();
                    popupMenu.show(table, x, y);
Debug.println("row: " + table.getSelectedRow());
                }
            }
        }
    };

    /** */
    static Properties props = new Properties();

    /* */
    static {
        final String path = "/JWinZip.properties";
        final Class<?> clazz = JWinZip.class;

        try {
            InputStream is = clazz.getResourceAsStream(path);
            props.load(is);
            is.close();

            Toolkit t = Toolkit.getDefaultToolkit();
            UIDefaults table = UIManager.getDefaults();

            int i = 0;
            while (true) {
                String key = "jWinZip.action." + i + ".iconName";
                String name = props.getProperty(key);
                if (name == null) {
                    Debug.println("no property for: jWinZip.action." + i + ".iconName");
                    break;
                }

                key = "jWinZip.action." + i + ".icon";
                String icon = props.getProperty(key);

                table.put(name, new ImageIcon(t.getImage(clazz.getResource(icon))));

                i++;
            }

            fileFilter = new RegexFileFilter();
        } catch (Exception e) {
            Debug.printStackTrace(e);
            System.exit(1);
        }
    }

    /** */
    private static RegexFileFilter fileFilter;

    /**
     * The program entry point.
     */
    public static void main(String[] args) throws Exception {
        new JWinZip(args);
    }
}

/* */
