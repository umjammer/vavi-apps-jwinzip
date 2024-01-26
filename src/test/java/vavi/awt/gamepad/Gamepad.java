/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.gamepad;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.java.games.input.usb.HidController;
import vavi.awt.joystick.hid4java.Hid4JavaEnvironmentPlugin;
import vavi.swing.binding.table.TableModel;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * Gamepad.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-01-24 nsano initial version <br>
 */
@PropsEntity(url = "file:local.properties")
public class Gamepad {

    static {
        // for fixing table rows count
        System.setProperty("net.java.games.input.InputEvent.fillAll", "true");
    }

    @Property(name = "mid")
    String mid;
    @Property(name = "pid")
    String pid;

    int vendorId;
    int productId;

    JFrame frame;
    JTable table;

    /** GUI */
    Gamepad() {
        frame = new JFrame();

        table = new JTable();
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane sp = new JScrollPane(table);

        frame.getContentPane().add(sp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(640, 800));
        frame.setTitle("Gamepad");
        frame.pack();
        frame.setVisible(true);
    }

    /** */
    void start() throws IOException {
        Hid4JavaEnvironmentPlugin environment = new Hid4JavaEnvironmentPlugin();
        HidController controller = environment.getController(vendorId, productId);

        GamepadModel gamepadModel = new GamepadModel(controller);
        TableModel<GamepadModel.VO> model = new TableModel<>(gamepadModel);
        gamepadModel.setUpdater(model::update);
        model.bind(table);
    }

    /** */
    public static void main(String[] args) throws Exception {
        Gamepad app = new Gamepad();
        PropsEntity.Util.bind(app);
        app.vendorId = Integer.decode(app.mid);
        app.productId = Integer.decode(app.pid);
        app.start();
    }
}
