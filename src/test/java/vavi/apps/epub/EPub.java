/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.epub;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import vavi.swing.binding.table.TableModel;
import vavi.util.Debug;


/**
 * EPub.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-02-07 nsano initial version <br>
 */
public class EPub {

    static {
        System.setProperty("vavi.util.logging.VaviFormatter.extraClassMethod", "(" +
                "sun\\.util\\.logging\\.internal\\.LoggingProviderImpl\\$JULWrapper#log" + "|" +
                "sun\\.util\\.logging\\.PlatformLogger#\\w+" +
                ")");
    }

    JFrame frame;
    JTable table;

    EPub() {
        frame = new JFrame();

        table = new JTable();
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane sp = new JScrollPane(table);

        frame.setTitle("EPub");

        frame.getContentPane().add(sp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(640, 800));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * @param args 0: dir
     */
    public static void main(String[] args) throws Exception {
        Path dir = Path.of(args[0]);
        EPubModel model = new EPubModel(dir);
        TableModel<?> tableModel = new TableModel<>(model);
        EPub app = new EPub();
        app.table.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if ((e.getModifiersEx() & InputEvent.META_DOWN_MASK) == InputEvent.META_DOWN_MASK &&
                        e.getKeyCode() == KeyEvent.VK_S) {
Debug.println("Saving...");
                    model.save();
                }
            }
        });
        tableModel.bind(app.table);
    }
}
