/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.gamepad;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import net.java.games.input.Event;
import net.java.games.input.WrappedComponent;
import net.java.games.input.usb.HidController;
import vavi.hid.parser.Field;
import vavi.net.www.MimeTable;
import vavi.swing.binding.table.Column;
import vavi.swing.binding.table.Row;
import vavi.swing.binding.table.Table;


/**
 * GamepadModel.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-01-23 nsano initial version <br>
 */
@Table(row = GamepadModel.ComponentModel.class, iterable = "entries")
public class GamepadModel {

    /** */
    private static final MimeTable mimeTable = new MimeTable();

    @Row(setter = "setVO")
    public static class ComponentModel {
        private VO vo;
        public void setVO(VO vo) {
            this.vo = vo;
        }
        @Column(sequence = 0, width = 200)
        public String getName() {
            return vo.name;
        }
        @Column(sequence = 1, width = 80, align = Column.Align.right)
        public String getMin() {
            return String.format("%d", vo.min);
        }
        @Column(sequence = 2, width = 80, align = Column.Align.right)
        public String getMax() {
            return String.format("%d", vo.max);
        }
        @Column(sequence = 3, width = 80, align = Column.Align.right)
        public String getValue() {
            return String.format("%10.3f", vo.value);
        }
    }

    /** */
    private HidController controller;

    // TODO mostly same as ComponentModel, it's possible to integrate?
    static class VO {

        String name;
        int min;
        int max;
        float value;

        public VO(String name, int min, int max, float value) {
            this.name = name;
            this.min = min;
            this.max = max;
            this.value = value;
        }

        @Override public String toString() {
            return new StringJoiner(", ", VO.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("min=" + min)
                    .add("max=" + max)
                    .add("value=" + value)
                    .toString();
        }
    }

    private List<VO> vos = new ArrayList<>();

    /** */
    public GamepadModel(HidController controller) {
        this.controller = controller;
    }

    /** */
    public void setUpdater(Runnable repaint) throws IOException {
        controller.addInputEventListener(e -> {
            Event event = new Event();
            vos.clear();
            while (e.getNextEvent(event)) {
                Field field = ((WrappedComponent<Field>) event.getComponent()).getWrappedObject();
                VO vo = new VO(event.getComponent().getName(), field.getLogicalMinimum(), field.getLogicalMaximum(), event.getValue());
                vos.add(vo);
            }
            repaint.run();
        });
        controller.open();
    }

    /** @see Table#iterable() */
    public Iterable<VO> entries() {
        return vos;
    }

    /** */
    private static String getFileName(String path) {
        return new File(path).getName();
    }

    /** */
    private static String getFilePath(String path) {
        path = new File(path).getParent();
        return path == null ? "" : path + File.separator;
    }
}
