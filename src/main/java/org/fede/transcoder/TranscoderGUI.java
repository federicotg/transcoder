/*
 * Copyright (C) 2015 Federico Tello Gentile <federicotg@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.fede.transcoder;

import static java.awt.GridBagConstraints.HORIZONTAL;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class TranscoderGUI extends JFrame implements Observer {

    private static final int CORES = Runtime.getRuntime().availableProcessors();

    /*modelos*/
    private File src;
    private File dst;
    private SpinnerNumberModel qualityModel;
    private DefaultBoundedRangeModel progressModel;
    private Transcoder transcoder;
    private boolean overwrite = false;
    private DefaultBoundedRangeModel coresModel;

    /*Actions*/
    private Action convertAction;
    private Action cancelAction;
    private Action sourceAction;
    private Action overwriteAction;
    private Action destinationAction;
    private JTextField srcLabel;
    private JTextField dstLabel;
    private JSpinner spinner;
    private JComboBox codecCombo;
    private JSlider slider;

    public TranscoderGUI() {
        super("Transcoder");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.initComponents();
        this.layoutComponents();
        this.pack();
        this.center();
    }

    private JTextField createTextField() {
        JTextField answer = new JTextField(30);
        answer.setEditable(false);
        answer.setFocusable(false);
        answer.setOpaque(false);
        answer.setCursor(null);
        answer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return answer;
    }

    private void initComponents() {
        AudioCodec defaultCodec = new AoTuVAudioCodec();
        this.qualityModel = new SpinnerNumberModel(
                defaultCodec.getDefaultQuality(),
                defaultCodec.getMinumumQuality(),
                defaultCodec.getMaximumQuality(),
                defaultCodec.getStepSize());

        
        if(CORES > 1){
            this.coresModel = new DefaultBoundedRangeModel(CORES, 0, 1, CORES);
        }

        this.progressModel = new DefaultBoundedRangeModel();

        this.cancelAction = new AbstractAction("Cancelar") {

            @Override
            public void actionPerformed(ActionEvent e) {
                transcoder.cancelTranscode();
                JOptionPane.showMessageDialog(getContentPane(), "Las tareas en curso no se pueden cancelar. Espere a que terminen normalmente. Ya no se inician nuevas tareas.", "Cancelando", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        this.cancelAction.setEnabled(false);

        this.convertAction = new AbstractAction("Convertir") {

            @Override
            public void actionPerformed(ActionEvent e) {
                progressModel.setValue(0);
                sourceAction.setEnabled(false);
                destinationAction.setEnabled(false);
                convertAction.setEnabled(false);
                cancelAction.setEnabled(true);
                spinner.setEnabled(false);
                codecCombo.setEnabled(false);
                slider.setEnabled(false);

                SwingWorker worker = new SwingWorker() {

                    @Override
                    protected Object doInBackground() throws Exception {
                        AudioCodec codec = (AudioCodec) codecCombo.getSelectedItem();
                        codec.setQuality(String.valueOf(qualityModel.getValue()));
                        transcoder = new Transcoder(codec, src, dst);
                        transcoder.addObserver(TranscoderGUI.this);
                        transcoder.transcodeInParallel(overwrite, getCoresToUse());
                        return null;
                    }

                    @Override
                    protected void done() {
                        sourceAction.setEnabled(true);
                        destinationAction.setEnabled(true);
                        convertAction.setEnabled(true);
                        cancelAction.setEnabled(false);
                        spinner.setEnabled(true);
                        codecCombo.setEnabled(true);
                        slider.setEnabled(true);
                    }
                };
                worker.execute();
            }
        };

        this.convertAction.setEnabled(false);

        this.sourceAction = new AbstractAction("Origen") {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setMultiSelectionEnabled(false);
                if(src != null){
                    chooser.setCurrentDirectory(src);
                }
                if (chooser.showOpenDialog(TranscoderGUI.this) == JFileChooser.APPROVE_OPTION) {
                    selectOrigin(chooser.getSelectedFile());
                }
            }
        };

        this.destinationAction = new AbstractAction("Destino") {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setMultiSelectionEnabled(false);
                if(dst != null){
                    chooser.setCurrentDirectory(dst);
                }
                if (chooser.showOpenDialog(TranscoderGUI.this) == JFileChooser.APPROVE_OPTION) {
                    dst = chooser.getSelectedFile();
                    dstLabel.setText(dst.getAbsolutePath());
                    updateConvertButtonState();
                }
            }
        };

        this.overwriteAction = new AbstractAction("Sobreescribir") {

            @Override
            public void actionPerformed(ActionEvent e) {
                overwrite = ((JCheckBox) e.getSource()).isSelected();
            }
        };

        AudioCodec[] codecs = new AudioCodec[]{new AoTuVAudioCodec(), new VorbisAudioCodec(), new LameMP3Codec()};

        this.codecCombo = new JComboBox(codecs);
        this.codecCombo.addActionListener((ActionEvent e) -> {
            AudioCodec selection = (AudioCodec) codecCombo.getSelectedItem();
            qualityModel = new SpinnerNumberModel(selection.getDefaultQuality(),
                    selection.getMinumumQuality(),
                    selection.getMaximumQuality(), selection.getStepSize());
            spinner.setModel(qualityModel);
        });
    }

    private int getCoresToUse(){
        if(CORES > 1){
            return this.coresModel.getValue();
        }
        return 1;
    }

    private void updateConvertButtonState() {
        this.convertAction.setEnabled(this.src != null
                && this.dst != null
                && !this.dst.equals(this.src)
                && this.src.exists()
                && this.dst.exists()
                && this.src.isDirectory()
                && this.dst.isDirectory()
                && this.progressModel.getMaximum() > 0);
    }

    private void addComponent(JComponent comp,
            GridBagConstraints gbc,
            int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        this.getContentPane().add(comp, gbc);
    }

    private void layoutComponents() {

        this.dstLabel = this.createTextField();
        this.srcLabel = this.createTextField();

        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = HORIZONTAL;

        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        this.addComponent(this.srcLabel, gbc, 0, 0);

        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        this.addComponent(new JButton(sourceAction), gbc, 2, 0);

        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        this.addComponent(this.dstLabel, gbc, 0, 1);

        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        this.addComponent(new JButton(this.destinationAction), gbc, 2, 1);

        this.addComponent(new JLabel("Códec"), gbc, 0, 2);
        this.addComponent(this.codecCombo, gbc, 1, 2);

        this.addComponent(new JLabel("Calidad"), gbc, 0, 3);
        this.spinner = new JSpinner();
        this.addComponent(this.spinner, gbc, 1, 3);
        this.spinner.setModel(qualityModel);

        if(CORES > 1){
            this.slider = new JSlider(this.coresModel);
            this.slider.setMajorTickSpacing(1);
            this.slider.setPaintTicks(true);
            this.slider.setPaintLabels(true);
            this.slider.setSnapToTicks(true);
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.PAGE_END;
            gbc.insets.bottom = 0;
            this.addComponent(new JLabel("Núcleos"), gbc, 2, 2);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.insets.bottom = 5;
            gbc.insets.top = 0;
            this.addComponent(this.slider, gbc, 2, 3);
            gbc.insets.top = 5;
        }

        JCheckBox cb = new JCheckBox(this.overwriteAction);
        cb.setSelected(this.overwrite);
        cb.setToolTipText("Seleccionar si se quieren sobreescribir los archivos que ya existen el la carpeta destino.");
        this.addComponent(cb, gbc, 0, 4);
        this.addComponent(new JButton(this.convertAction), gbc, 1, 4);

        this.addComponent(new JButton(this.cancelAction), gbc, 2, 4);

        JProgressBar progressBar = new JProgressBar(this.progressModel);
        progressBar.setStringPainted(true);
        gbc.gridwidth = 3;
        this.addComponent(progressBar, gbc, 0, 5);

    }

    private void center() {
        final GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        final Point center = ge.getCenterPoint();
        final Rectangle bounds = ge.getMaximumWindowBounds();

        final int w = this.getWidth();
        final int h = this.getHeight();

        final int x = center.x - w / 2;
        final int y = center.y - h / 2;
        this.setBounds(x, y, w, h);

        if (w == bounds.width && h == bounds.height) {
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
    }

    private void selectOrigin(File selection) {
        this.src = selection;
        this.srcLabel.setText(src.getAbsolutePath());
        SwingWorker<Integer, Object> countFiles = new SwingWorker<Integer, Object>() {

            private int fileCount;

            @Override
            protected Integer doInBackground() throws Exception {
                this.fileCount = countFiles();
                return this.fileCount;
            }

            @Override
            protected void done() {
                progressModel.setMaximum(this.fileCount);
                progressModel.setValue(0);
                srcLabel.setText(srcLabel.getText() + " (" + this.fileCount + ")");
                updateConvertButtonState();
            }
        };
        countFiles.execute();
    }

    private int countFiles(int total, File current, FilenameFilter flacFilter, FileFilter directoryFilter) {
        int count = total + current.list(flacFilter).length;

        for (File subdir : current.listFiles(directoryFilter)) {
            count = this.countFiles(count, subdir, flacFilter, directoryFilter);
        }
        return count;
    }

    private int countFiles() {
        int total = 0;
        return countFiles(total, this.src, new FileExtensionFilter(".flac"), new DirectoryFilter());

    }

    @Override
    public void update(Observable o, final Object val) {

        if ((Integer) val != 0) {
            transcoder.cancelTranscode();
            transcoder.deleteObserver(this);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    JOptionPane.showMessageDialog(getContentPane(), "Error", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else {

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    progressModel.setValue(progressModel.getValue() + 1);
                }
            });
        }
    }
}
