/*
 *	Paquete				: gui
 *	Archivo				: ConfiguracionPerfil.java
 *	Version				: 1.0.0		2007-07-05
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.gui;

import javax.help.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;

import org.jdom.*;

import com.avpsoft.icvoz.archivo.*;
import com.avpsoft.icvoz.main.Main;
import com.avpsoft.icvoz.rna.*;

public class ConfiguracionPerfil extends JPanel {

    private String perfil;
    private ArchivoRNA archivoRNA;
    private MyTable myTable;

    private Configuracion parent;
    public JPanel target;

    private JButton atras;
    private JButton aplicar;

    private JScrollPane scrollTable;

    public ConfiguracionPerfil(String perfil, JPanel target, Configuracion parent) {

        super(new BorderLayout());

        this.perfil = perfil;
        this.target = target;
        this.parent = parent;

        ImageIcon imgEdicion = new javax.swing.ImageIcon(getClass().getResource("/img/configure.png"));
        JLabel titulo = new JLabel("Edicion del Perfil", imgEdicion, JLabel.CENTER);

        JLabel superior = new JLabel(Configuracion.imgFondo375x50);
        superior.setLayout(new BorderLayout());
        superior.setPreferredSize(new Dimension(375, 50));
        superior.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        superior.add(titulo, BorderLayout.CENTER);

        JLabel centroSuperior = new JLabel(Configuracion.imgFondo375x40);
        centroSuperior.setLayout(new BorderLayout());
        centroSuperior.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        centroSuperior.add(new JLabel("Edicion del perfil : " + this.perfil), BorderLayout.CENTER);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setPreferredSize(new Dimension(375, 260));
        centro.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        centro.add(centroSuperior, BorderLayout.NORTH);

        ImageIcon imgAyuda = new javax.swing.ImageIcon(getClass().getResource("/img/help.png"));
        ImageIcon imgCancelar = new javax.swing.ImageIcon(getClass().getResource("/img/no.png"));
        ImageIcon imgAceptar = new javax.swing.ImageIcon(getClass().getResource("/img/ok.png"));
        ImageIcon imgAtras = new javax.swing.ImageIcon(getClass().getResource("/img/back.png"));

        try {

            myTable = new MyTable(perfil);
            archivoRNA = new ArchivoRNA(this.perfil);

            scrollTable = new JScrollPane();
            scrollTable.setPreferredSize(new Dimension(375, 216));
            scrollTable.getViewport().add(myTable);

            aplicar = new JButton(imgAceptar);
            aplicar.setToolTipText("Aceptar");
            aplicar.setHorizontalTextPosition(JButton.LEFT);
            aplicar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    entrenar();
                }
            });

            centro.add(scrollTable, BorderLayout.CENTER);
        } catch (Exception e) {
            System.out.println("Error: ConfigurarPerfil: setPerfil");
        }

        atras = new JButton(imgAtras);
        atras.setToolTipText("Atras");
        atras.setHorizontalTextPosition(JButton.LEFT);
        atras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                atras();
            }
        });

        JButton cancelar = new JButton(imgCancelar);
        cancelar.setToolTipText("Cancelar");
        cancelar.setHorizontalTextPosition(JButton.LEFT);
        cancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                finalizar();
            }
        });

        JButton ayuda = new JButton(imgAyuda);
        ayuda.setToolTipText("Ayuda");
        ayuda.setHorizontalTextPosition(JButton.LEFT);
        CSH.setHelpIDString(ayuda, "cmd.descripcion");
        ayuda.addActionListener(new CSH.DisplayHelpFromSource(Main.helpBroker));

        JLabel botones = new JLabel(Configuracion.imgFondo375x40);
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setPreferredSize(new Dimension(375, 40));
        botones.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        botones.add(atras);
        botones.add(aplicar);
        botones.add(cancelar);
        botones.add(ayuda);

        add(superior, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        repaint();
    }

    public void atras() {
        target.removeAll();
        target.add(parent.principal);
        target.repaint();
    }

    public void entrenar() {
        Task task = new Task();
        task.execute();
        task = null;
    }

    public void aplicarCambios(JProgressBar progreso) {
        try {
            RedNeuronal redNeuronal = new RedNeuronal(this.perfil);
            redNeuronal.entrenamiento(progreso);
        } catch (Exception e) {
            System.out.println("Error: ConfiguracionPerfil: aplicarCambios");
        }
        JOptionPane.showMessageDialog(null, "Ha finalizado el proceso", "Aplicar Cambios", JOptionPane.INFORMATION_MESSAGE);
    }

    public void inicio() {
        parent.inicio();
    }

    public void finalizar() {
        parent.finalizar();
        repaint();
    }

    private class MyTable extends JPanel {

        private JCheckBox activo[];
        private ArchivoCONFIG aConfig;
        private java.util.List cmd;

        public MyTable(String perfil) {
            super(new SpringLayout());
            ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"));
            try {

                aConfig = new ArchivoCONFIG(perfil);

                cmd = aConfig.getComandos();

                activo = new JCheckBox[cmd.size()];

                add(new JLabel("Activo", JLabel.CENTER));
                add(new JLabel("Nombre", JLabel.CENTER));
                add(new JLabel("Editar", JLabel.CENTER));

                for (int i = 0; i < cmd.size(); i++) {
                    final int c = i;
                    activo[i] = new JCheckBox();
                    activo[i].setSelected(new Boolean(((Element) cmd.get(i)).getChild("habilitado").getText()).booleanValue());
                    //activo[i].setEnabled(false);
                    activo[i].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            habilitar(((Element) cmd.get(c)).getAttributeValue("codigo"), c);
                        }
                    });
                    JPanel checkbox = new JPanel();
                    checkbox.add(activo[i]);
                    add(checkbox);
                    JPanel label = new JPanel();
                    JLabel labelNombre = new JLabel(((Element) cmd.get(i)).getChild("nombre").getText());
                    labelNombre.setToolTipText(((Element) cmd.get(i)).getChild("descripcion").getText());
                    label.add(labelNombre);
                    add(label);

                    JButton accion = new JButton(img);
                    accion.setToolTipText("Editar");
                    accion.setHorizontalTextPosition(JButton.LEFT);
                    accion.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            configurar((Element) cmd.get(c), c);
                        }
                    });
                    JPanel boton = new JPanel();
                    boton.add(accion);
                    add(boton);
                }
                //Layout the panel.
                SpringUtilities.makeGrid(this,
                        cmd.size() + 1, 3, //rows, cols
                        5, 5, //initialX, initialY
                        5, 5);//xPad, yPad

            } catch (Exception e) {
                System.out.println("Error: configurarPerfil:constructor");
            }
        }

        public void habilitar(String cmd, int i) {
            if (activo != null) {
                try {
                    Object[] options = {"SI", "NO"};
                    int op = JOptionPane.showOptionDialog(null, "Ha " + (activo[i].isSelected() ? "habilitado" : "desabilitado") + " el comando de voz ", "Habilitar/Desabilitar Comando",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

                    if (op == 0) {
                        System.out.println("Habilitar CMD " + cmd);
                        aConfig.setHabilitado(cmd, activo[i].isSelected());
                        aConfig.guardar();
                    } else {
                        activo[i].setSelected(!activo[i].isSelected());
                    }

                } catch (Exception e) {
                    System.out.println("Error: ConfigurarPerfil:habilitar");
                    e.printStackTrace();
                }
            }
        }
    }

    public void configurar(Element e, int i) {
        System.out.println(i + " ==> " + e.getChild("nombre").getText());

        ConfiguracionCMD cmd = new ConfiguracionCMD(archivoRNA, e, perfil, this);

        target.removeAll();
        target.add(cmd);
        target.revalidate();
        target.repaint();
    }

    private class Progreso extends JDialog {

        private JProgressBar animacion;
        private JProgressBar progreso;
        private JPanel panel;
        private JPanel barras;

        public Progreso() {
            super(new Frame(), "Espere ...", true);

            ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/img/run.png"));
            JLabel mensaje = new JLabel("<html><body><blockquote>Espere mientras se realizan <br> los cambios respectivos</blockquote></body></html>", img, JLabel.CENTER);

            panel = new JPanel(new BorderLayout());
            panel.add(mensaje, BorderLayout.CENTER);

            progreso = new JProgressBar(0, 100);
            progreso.setIndeterminate(false);
            progreso.setStringPainted(true);

            animacion = new JProgressBar(0, 100);
            animacion.setIndeterminate(true);

            barras = new JPanel(new BorderLayout());
            barras.add(progreso, BorderLayout.NORTH);
            barras.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.CENTER);
            barras.add(animacion, BorderLayout.SOUTH);

            panel.add(barras, BorderLayout.SOUTH);
            add(panel);

            setResizable(false);
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            setBounds((screenSize.width - 300) / 2, (screenSize.height - 150) / 2, 300, 150);
        }

        public JProgressBar getProgreso() {
            return progreso;
        }
    }

    private class Task extends SwingWorker<Void, Void> {

        public Progreso progreso = new Progreso();

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progreso.setVisible(true);
                }
            });
            aplicarCambios(progreso.getProgreso());
            progreso.setVisible(false);
            return null;
        }

        /*
         * Executed in event dispatch thread
         */
        public void done() {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
