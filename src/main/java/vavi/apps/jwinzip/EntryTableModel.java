/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.jwinzip;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import vavi.net.www.MimeTable;
import vavi.util.archive.Archive;
import vavi.util.archive.Entry;


/**
 * EntryTableModel
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 021103 nsano initial version <br>
 *          0.01 030211 nsano exclude directory <br>
 */
public class EntryTableModel extends AbstractTableModel {

    /** カラムの名前 */
    public static final String[] columnNames = {
        "Name", "Type", "Modified", "Size", "Ratio", "Packed", "Path"
    };

    /** */
    public static final int[] widths = {
        150, 80, 100, 60, 40, 60, 320
    };

    /** */
    private MimeTable mimeTable = new MimeTable();

    /** model */
    private Entry[] entries;

    /** テーブルモデルを構築します． */
    public EntryTableModel(Archive archive) {
        List<Entry> list = new ArrayList<>();
        Entry[] entries = archive.entries();
        for (Entry entry : entries) {
            if (!entry.isDirectory()) {
                list.add(entry);
            }
        }
        this.entries = new Entry[list.size()];
        list.toArray(this.entries);
    }

    // -------------------------------------------------------------------------

    /** カラム数を取得します． */
    public int getColumnCount() {
        return columnNames.length;
    }

    /** カラム名を取得します． */
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    /** 行数を取得します． */
    public int getRowCount() {
        return entries.length;
    }

    /** 指定したカラム，行にある値を取得します． */
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            return getName(entries[rowIndex].getName());
        case 1:
            return mimeTable.getContentTypeFor(getName(entries[rowIndex].getName()));
        case 2:
            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            return sdf.format(new Date(entries[rowIndex].getTime()));
        case 3:
            return entries[rowIndex].getSize();
        case 4:
            long originalSize = entries[rowIndex].getSize();
            long packedSize = entries[rowIndex].getCompressedSize();
            return (int) ((float) packedSize / originalSize * 100) + "%";
        case 5:
            return entries[rowIndex].getCompressedSize();
        case 6:
            return getPath(entries[rowIndex].getName());
        default:
            throw new IllegalArgumentException();
        }
    }

    /** */
    private String getName(String path) {
        return new File(path).getName();
    }

    /** */
    private String getPath(String path) {
        path = new File(path).getParent();
        return path == null ? "" : path + File.separator;
    }
}

/* */
