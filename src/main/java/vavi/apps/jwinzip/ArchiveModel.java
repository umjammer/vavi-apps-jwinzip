/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.jwinzip;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import vavi.net.www.MimeTable;
import vavi.swing.binding.table.Column;
import vavi.swing.binding.table.Row;
import vavi.swing.binding.table.Table;
import vavi.util.archive.Archive;
import vavi.util.archive.Entry;


/**
 * ArchiveModel.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-12-02 nsano initial version <br>
 */
@Table(row = ArchiveModel.ArchiveEntryModel.class, iterable = "entries")
public class ArchiveModel {

    /** */
    private static final MimeTable mimeTable = new MimeTable();

    @Row(setter = "setEntry")
    public static class ArchiveEntryModel {
        private Entry entry;
        public void setEntry(Entry entry) {
            this.entry = entry;
        }
        @Column(sequence = 0, width = 150)
        public String getName() {
            return getFileName(entry.getName());
        }
        @Column(sequence = 1, width = 80)
        public String getType() {
            return mimeTable.getContentTypeFor(getFileName(entry.getName()));
        }
        @Column(sequence = 2, width = 100)
        public String getModified () {
            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            return sdf.format(new Date(entry.getTime()));
        }
        @Column(sequence = 3, width = 60)
        public long getSize() {
            return entry.getSize();
        }
        @Column(sequence = 4, width = 40)
        public String getRatio() {
            long originalSize = entry.getSize();
            long packedSize = entry.getCompressedSize();
            return (int) ((float) packedSize / originalSize * 100) + "%";
        }
        @Column(sequence = 5, width = 60)
        public long getPacked() {
            return entry.getCompressedSize();
        }
        @Column(sequence = 6, width = 320)
        public String getPath() {
            return getFilePath(entry.getName());
        }
    }

    /** */
    private Archive archive;

    /** */
    public ArchiveModel(Archive archive) {
        this.archive = archive;
    }

    /** */
    public Iterable<Entry> entries() {
        return Arrays.asList(archive.entries());
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
