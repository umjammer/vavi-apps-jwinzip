/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.epub;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

import vavi.apps.epub.EPubModel.VO;
import vavi.swing.binding.table.Column;
import vavi.swing.binding.table.Row;
import vavi.swing.binding.table.Table;
import vavi.text.epub.Epub3;
import vavi.text.epub.Epub3.Content;
import vavi.util.Debug;


/**
 * EPubModel.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-02-07 nsano initial version <br>
 */
@Table(row = VO.class, iterable = "entries")
public class EPubModel {

    /** empty image */
    static BufferedImage noImage;

    static {
        try {
            noImage = ImageIO.read(EPubModel.class.getResourceAsStream("/broken.png"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Row(setter = "setVO")
    public static class ComponentModel {
        private VO vo;
        public void setVO(VO vo) {
            this.vo = vo;
        }
        @Column(sequence = 0, width = 200, editable = true)
        public String getTitle() {
            return vo.content.title;
        }
        @Column(sequence = 1, width = 80, editable = true)
        public String getAuthor() {
            return vo.content.author;
        }
        @Column(sequence = 2, width = 80)
        public BufferedImage getImage() {
            return vo.image;
        }
        @Column(sequence = 0)
        public void setTitle(String value) {
Debug.println("setTitle: " + value);
            vo.content.title = value; // TODO modify xml document model
            vo.dirty = true;
        }
        @Column(sequence = 1)
        public void setAuthor(String value) {
Debug.println("setAuthor: " + value);
            vo.content.author = value; // TODO modify xml document model
            vo.dirty = true;
        }
    }

    public static class VO {
        boolean dirty;
        Content content;
        BufferedImage image = noImage;

        public VO(Epub3 epub3) {
            try {
                this.content = epub3.getContent();
                BufferedImage image = content.getImage();
                if (image != null) {
                    this.image = image;
                }
            } catch (IOException e) {
                Debug.printStackTrace(e);
            }
        }
    }

    /** rows */
    private final List<VO> epubs = new ArrayList<>();

    /** {@link Table} */
    public Iterable<VO> entries() {
        return epubs;
    }

    /** */
    public void save() {
        epubs.forEach(vo -> {
            if (vo.dirty) {
                try {
                    vo.content.updateMetadata();
                    vo.content.write();
Debug.println("written: [" + vo.content.author + "] " + vo.content.title);
                } catch (IOException e) {
                    Debug.println(Level.INFO, e);
                }
            }
        });
    }

    /** @param dir target directory (not recursive) */
    public EPubModel(Path dir) {
        try {
            try (Stream<Path> files = Files.list(dir)) {
                files.sorted().forEach(p -> {
                    try {
                        Epub3 epub3 = new Epub3(p);
                        epubs.add(new VO(epub3));
                        Runtime.getRuntime().addShutdownHook(new Thread(() -> { try { /* System.err.println("epub3.close"); */ epub3.close(); } catch (IOException e) { System.err.println(e.getMessage()); }}));
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
