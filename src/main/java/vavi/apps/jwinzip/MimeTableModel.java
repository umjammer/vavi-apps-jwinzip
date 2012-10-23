/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.jwinzip;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import vavi.net.www.MimeTable;


/**
 * MimeTableModel
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 021103 nsano initial version <br>
 */
public class MimeTableModel extends AbstractTableModel {

    /** カラムの名前 */
    public static final String columnName[] = {
        "type",
        "image",
        "action",
        "command",
        "extensions",
        "description"
    };

    /** */
    public static final int[] widths = { 80, 40, 40, 180, 180, 320 };

    /** */
    private Map<?, ?> entries = new HashMap<Object, Object>();

    /** テーブルモデルを構築します． */
    public MimeTableModel(MimeTable mimeTable) {
        entries = mimeTable.entries();
    }

    //-------------------------------------------------------------------------

    /** カラム数を取得します． */
    public int getColumnCount() {
        return columnName.length;
    }

    /** カラム名を取得します． */
    public String getColumnName(int columnIndex) {
        return columnName[columnIndex];
    }

    /** 行数を取得します． */
    public int getRowCount() {
        return entries.size();
    }

    /** 指定したカラム，行にある値を取得します． */
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0: // contentType
            return null;
        case 1: // image
        case 2: // action
        case 3: // command
        case 4: // extensions
        case 5: // description
            return null;
        default:
            throw new IllegalArgumentException(String.valueOf(columnIndex));
        }
    }
}

/* */
