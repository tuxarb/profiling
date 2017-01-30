package app.view.gui;

import app.utils.Log;
import app.utils.Utils;
import org.slf4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class ProgressDialog {
    private JFrame view;
    private final JDialog dialog = new JDialog(view, Log.PROCESSING_GUI, false);
    private static final Logger LOG = Log.createLog(ProgressDialog.class);

    ProgressDialog(GuiView guiView) {
        this.view = guiView;
        init();
        LOG.info(Log.PROCESS_INFO_START);
    }

    private void init() {
        ImagePanel imagePanel = new ImagePanel();
        imagePanel.init();
        dialog.getContentPane().add(imagePanel);
        dialog.setUndecorated(false);
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.setSize(175, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setVisible(true);
        dialog.setAlwaysOnTop(true);
        dialog.validate();
    }

    void closeDialog() {
        LOG.info(Log.PROCESS_INFO_END);
        if (dialog.isVisible()) {
            dialog.getContentPane().removeAll();
            dialog.getContentPane().validate();
            dialog.setVisible(false);
        }
    }

    private class ImagePanel extends JPanel {
        Image image;

        ImagePanel() {
        }

        void init() {
            image = Toolkit.getDefaultToolkit().createImage(
                    getBytesStream().toByteArray()
            );
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, this);
            }
        }

        private ByteArrayOutputStream getBytesStream()
        {
            BufferedInputStream bis = new BufferedInputStream(
                    ((GuiView) view).getStreamOfResourceFile(Utils.PROGRESS)
            );
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                int ch;
                while ((ch = bis.read()) != -1) {
                    baos.write(ch);
                }
            } catch (IOException e) {
                LOG.warn(Log.LOADING_PROGRESS_IMAGE_ERROR);
            }
            return baos;
        }
    }
}