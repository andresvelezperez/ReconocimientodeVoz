/*
 *	Paquete				: gui
 *	Archivo				: ConfiguracionCMD.java
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
import java.io.*;

import org.jdom.*;

import com.avpsoft.icvoz.archivo.*;
import com.avpsoft.icvoz.captura.*;
import com.avpsoft.icvoz.espectro.*;
import com.avpsoft.icvoz.audio.*;
import com.avpsoft.icvoz.main.Main;

public class ConfiguracionCMD extends JPanel {

    private Element cmd;
    private ArchivoRNA archivoRNA;
    private String perfil;
    private CapturaMicrofono capturaMicrofono;
    public ConfiguracionPerfil parent;
    private Grafica grafica;

    private int indice = 0;

    public ConfiguracionCMD(ArchivoRNA archivoRNAs, Element cmds, String prfl, ConfiguracionPerfil parents) {
        super(new BorderLayout());

        this.parent = parents;
        this.archivoRNA = archivoRNAs;
        this.cmd = cmds;
        this.perfil = prfl;
        System.out.println("CMD   : " + cmd.getAttributeValue("codigo"));

        ImageIcon imgEdicion = new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"));
        JLabel titulo = new JLabel("Edicion del Comando de Voz", imgEdicion, JLabel.CENTER);

        JLabel superior = new JLabel(Configuracion.imgFondo375x50);
        superior.setLayout(new BorderLayout());
        superior.setPreferredSize(new Dimension(375, 50));
        superior.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        superior.add(titulo, BorderLayout.CENTER);

        JScrollPane scrollTable = new JScrollPane(Contenido());

        JPanel centro = new JPanel(new BorderLayout());
        centro.setPreferredSize(new Dimension(375, 260));
        centro.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        centro.add(scrollTable, BorderLayout.CENTER);

        ImageIcon imgAyuda = new javax.swing.ImageIcon(getClass().getResource("/img/help.png"));
        ImageIcon imgCancelar = new javax.swing.ImageIcon(getClass().getResource("/img/no.png"));
        ImageIcon imgAceptar = new javax.swing.ImageIcon(getClass().getResource("/img/ok.png"));
        ImageIcon imgAtras = new javax.swing.ImageIcon(getClass().getResource("/img/back.png"));

        JButton atras = new JButton(imgAtras);
        atras.setToolTipText("Atras");
        atras.setHorizontalTextPosition(JButton.LEFT);
        atras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                atras();
            }
        });

        JButton apply = new JButton(imgAceptar);
        apply.setToolTipText("Aceptar");
        apply.setHorizontalTextPosition(JButton.LEFT);
        apply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Object[] options = {"SI", "NO"};
                int op = JOptionPane.showOptionDialog(null, "Desea Guardar los cambios", "Comandos",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (op == 0) {
                    op = JOptionPane.showOptionDialog(null, "Desea Configurar Otro Comando", "Comandos",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (op == 1) {
                        try {
                            archivoRNA.guardar();
                        } catch (Exception e) {
                            System.out.println("Error: ConfiguracionCMD:EventoAceptar");
                        }
                        if (capturaMicrofono != null) {
                            Wav.grabar(perfil, cmd.getAttributeValue("nombre"), capturaMicrofono.getAudio().toByteArray());
                        }

                        parent.entrenar();
                        finalizar();
                    } else {
                        if (capturaMicrofono != null) {
                            Wav.grabar(perfil, cmd.getAttributeValue("nombre"), capturaMicrofono.getAudio().toByteArray());
                        }

                        atras();
                    }
                } else {
                    finalizar();
                }
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
        CSH.setHelpIDString(ayuda, "cmd.modificacion");
        ayuda.addActionListener(new CSH.DisplayHelpFromSource(Main.helpBroker));

        JLabel botones = new JLabel(Configuracion.imgFondo375x40);
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setPreferredSize(new Dimension(375, 40));
        botones.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        botones.add(atras);
        botones.add(apply);
        botones.add(cancelar);
        botones.add(ayuda);

        add(superior, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    public void atras() {
        parent.target.removeAll();
        parent.target.add(parent);
        parent.target.repaint();
    }

    private void procesar(ByteArrayOutputStream byteArrayOutputStream) {
        System.out.println("Procesando.............");
        DetectarBordes detectarBordes = new DetectarBordes(byteArrayOutputStream);
        detectarBordes.detector();
        Espectro espectro = new Espectro();
        double salida[] = espectro.espectro(detectarBordes.getDatos().leerMatrix());
        /*RedNeuronal rna = new RedNeuronal(this.perfil);
    	int s = rna.redNeuronal(salida);
    	System.out.println("Comando: "+s);/*/
        try {
            archivoRNA.setEntrada(salida, cmd.getAttributeValue("codigo") + String.valueOf((char) (97 + indice)));
        } catch (Exception e) {
            System.out.println("Error: ConfiguracionCMD:procesar");
        }
        /**/
        System.out.println("Proceso realizado");
    }

    private JPanel Contenido() {
        JLabel label1 = new JLabel("Esta modificando el perfil:  " + this.perfil);
        JLabel label2 = new JLabel("Esta editando el comando:  " + cmd.getChild("nombre").getText());

        JLabel superiorS = new JLabel(Configuracion.imgFondo375x40);
        superiorS.setLayout(new BorderLayout());
        superiorS.setPreferredSize(new Dimension(365, 40));
        superiorS.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        superiorS.add(label1);

        JLabel superiorC = new JLabel(Configuracion.imgFondo375x40);
        superiorC.setLayout(new BorderLayout());
        superiorC.setPreferredSize(new Dimension(365, 40));
        superiorC.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        superiorC.add(label2);

        JPanel superior = new JPanel();
        superior.setLayout(new BorderLayout());
        superior.setPreferredSize(new Dimension(365, 80));
        //superior.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        superior.add(superiorS, BorderLayout.NORTH);
        superior.add(superiorC, BorderLayout.CENTER);

        ImageIcon imgStart = new javax.swing.ImageIcon(getClass().getResource("/img/player_rec.png"));
        ImageIcon imgStop = new javax.swing.ImageIcon(getClass().getResource("/img/player_stop.png"));
        ImageIcon imgPlay = new javax.swing.ImageIcon(getClass().getResource("/img/player_play.png"));

        JLabel botones = new JLabel(Configuracion.imgFondo375x40);
        botones.setLayout(new FlowLayout(FlowLayout.CENTER));
        botones.setPreferredSize(new Dimension(365, 40));
        botones.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        final JButton start = new JButton(imgStart);
        start.setToolTipText("Grabar");
        final JButton stop = new JButton(imgStop);
        stop.setToolTipText("Parar");
        stop.setEnabled(false);
        final JButton play = new JButton(imgPlay);
        play.setToolTipText("Reproducir");
        play.setHorizontalTextPosition(JButton.LEFT);
        play.setEnabled(false);

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                start.setEnabled(false);
                stop.setEnabled(true);
                play.setEnabled(false);
                iniciar();
            }
        });

        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (indice < 0)//
                {
                    parar();
                    indice++;
                    JOptionPane.showMessageDialog(null, "Porfavor repita el comando.", "Comandos", JOptionPane.WARNING_MESSAGE);
                    iniciar();
                } else {
                    start.setEnabled(true);
                    stop.setEnabled(false);
                    play.setEnabled(true);
                    parar();
                    indice = 0;
                }
            }
        });

        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                reproducir();
            }
        });
        botones.add(start);
        botones.add(stop);
        botones.add(play);

        grafica = new Grafica();
        grafica.setPreferredSize(new Dimension(30, 120));
        grafica.setBackground(Color.black);
        grafica.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(superior, BorderLayout.NORTH);
        centro.add(grafica, BorderLayout.CENTER);
        centro.add(botones, BorderLayout.SOUTH);

        return centro;
    }

    public void iniciar() {
        capturaMicrofono = null;
        capturaMicrofono = new CapturaMicrofono(new ByteArrayOutputStream());
        /**/
        capturaMicrofono.start();
        if (grafica != null) {
            grafica.start();
            capturaMicrofono.setGrafica(grafica);
        }/**/
    }

    public void parar() {
        if (capturaMicrofono != null) {
            capturaMicrofono.stop();
            if (grafica != null) {
                grafica.stop();
            }
            procesar(capturaMicrofono.getAudio());
            /*/
    		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    		byte dat[] = Wav.leerArchivo(cmd.getAttributeValue("nombre")).toByteArray();
			byteArrayOutputStream.reset();
			byteArrayOutputStream.write(dat,0,dat.length);
			procesar(byteArrayOutputStream);	/**/
        }
    }

    public void reproducir() {
        if (capturaMicrofono != null) {
            Wav.reproducir((byte[]) capturaMicrofono.getAudio().toByteArray().clone());
        } else
			;//Wav.reproducir(cmd.getAttributeValue("nombre"));
    }

    public void inicio() {
        parent.inicio();
    }

    public void finalizar() {
        parent.finalizar();
    }
}
