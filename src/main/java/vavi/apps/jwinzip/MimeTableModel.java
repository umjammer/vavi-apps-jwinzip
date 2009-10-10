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

    /** �J�����̖��O */
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

    /** �e�[�u�����f�����\�z���܂��D */
    public MimeTableModel(MimeTable mimeTable) {
        entries = mimeTable.entries();
    }

    //-------------------------------------------------------------------------

    /** �J���������擾���܂��D */
    public int getColumnCount() {
        return columnName.length;
    }

    /** �J���������擾���܂��D */
    public String getColumnName(int columnIndex) {
        return columnName[columnIndex];
    }

    /** �s�����擾���܂��D */
    public int getRowCount() {
        return entries.size();
    }

    /** �w�肵���J�����C�s�ɂ���l���擾���܂��D */
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
