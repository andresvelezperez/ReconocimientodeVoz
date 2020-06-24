/*
 *	Paquete				: main
 *	Archivo				: Main.java
 *	Version				: 1.0.0		2007-02-22
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.help.*;
import javax.swing.*;

import org.jdom.*;

import com.avpsoft.icvoz.archivo.*;
import com.avpsoft.icvoz.audio.Audio;
import com.avpsoft.icvoz.gui.*;

public class Main {

    private Inicio inicio;
    private Configuracion config;

    private static TrayIcon trayIcon;

    private Image imageStart = new javax.swing.ImageIcon(getClass().getResource("/img/icvoz22x22.png")).getImage();
    private Image imageStop = new javax.swing.ImageIcon(getClass().getResource("/img/icvoz22x22s.png")).getImage();

    private PopupMenu popup;

    private MenuItem salir, iniciar, parar, reiniciar, configurar, ayuda, acerca;

    public static HelpBroker helpBroker;

    public static boolean DEBUG = false;
    public static boolean VERBOSE = false;
    public static boolean EMULACION = true;

    public Main() {
        helpBroker = getHelpSet("ayuda/IcVoz.hs").createHelpBroker();

        popup = new PopupMenu();

        salir = new MenuItem("Salir");
        salir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (!config.isVisible()) {
                    stop();
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor cierre primero el asistente", "Salir", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        iniciar = new MenuItem("Iniciar");
        iniciar.setEnabled(true);
        iniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!config.isVisible()) {
                    start();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor cierre primero el asistente", "Iniciar", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        parar = new MenuItem("Parar");
        parar.setEnabled(false);
        parar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        reiniciar = new MenuItem("Reiniciar");
        reiniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!config.isVisible()) {
                    restart();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor cierre primero el asistente", "Iniciar", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        configurar = new MenuItem("Configurar");
        configurar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stop();
                configuracion();
            }
        });

        ayuda = new MenuItem("Ayuda");
        CSH.setHelpIDString(ayuda, "top");
        ayuda.addActionListener(new CSH.DisplayHelpFromSource(Main.helpBroker));

        acerca = new MenuItem("Acerca de ...");
        acerca.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, new Acerca(), "Acerca de ....", JOptionPane.PLAIN_MESSAGE);
            }
        });

        popup.add(iniciar);
        popup.add(parar);
        popup.add(reiniciar);
        popup.add(new MenuItem("-"));
        popup.add(configurar);
        popup.add(new MenuItem("-"));
        popup.add(ayuda);
        popup.add(acerca);
        popup.add(new MenuItem("-"));
        popup.add(salir);

        config = new Configuracion();
        config.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        config.pack();

        SystemTrayIcon();
        if (trayIcon != null) {
            trayIcon.setImage(parar.isEnabled() ? imageStart : imageStop);
        }
    }

    public void start() {
        try {

            Archivo archivo = new Archivo();
            Document doc = archivo.getDocument();
            Element raiz = doc.getRootElement();
            Element usuario = raiz.getChild("perfil");
            String perfil = usuario.getText();
            inicio = new Inicio(perfil);
            inicio.start();
            iniciar.setEnabled(false);
            parar.setEnabled(true);
            if (trayIcon != null) {
                trayIcon.setImage(imageStart);
            }
        } catch (Exception e) {
            System.out.println("Error: Main:start: " + e);
        }
    }

    public void stop() {
        if (inicio != null) {
            inicio.close();
        }
        inicio = null;
        iniciar.setEnabled(true);
        parar.setEnabled(false);
        if (trayIcon != null) {
            trayIcon.setImage(imageStop);
        }
        System.gc();
    }

    public void restart() {
        stop();
        start();
        iniciar.setEnabled(false);
        parar.setEnabled(true);
    }

    public void configuracion() {
        config.setVisible(true);
    }

    public void SystemTrayIcon() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(imageStop, "Interpretador de Comandos de Voz", popup);
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mensageInfo("ICVOZ", "Interpretador de Comandos de Voz");
                }
            };
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {
            System.err.println("System tray is currently not supported.");
        }
    }

    public HelpSet getHelpSet(String helpsetfile) {
        HelpSet hs = null;
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
            hs = new HelpSet(null, hsURL);
        } catch (Exception ee) {
            System.out.println("HelpSet: " + ee.getMessage());
            System.out.println("HelpSet: " + helpsetfile + " not found");
        }
        return hs;
    }

    public static void mensageInfo(String titulo, String mensage) {
        Main.trayIcon.displayMessage(titulo, mensage, TrayIcon.MessageType.INFO);
    }

    public static void mensageError(String titulo, String mensage) {
        Main.trayIcon.displayMessage(titulo, mensage, TrayIcon.MessageType.ERROR);
    }

    static {
        System.setProperty("icvoz.version", "0.0.1");
        System.setProperty("icvoz.audio.format", "" + Audio.getAudioFormat());
    }

    public static void main(String args[]) {

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
            if (args[i].compareTo("-d") == 0) {
                Main.DEBUG = true;
                Main.VERBOSE = true;
            }

            if (args[i].compareTo("-v") == 0) {
                Main.VERBOSE = true;
            }

            if (args[i].compareTo("-e") == 0) {
                Main.EMULACION = false;
            }
        }

        if (Main.DEBUG) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    GUI.makeGUI();
                }
            });
        }

        if (!Main.VERBOSE) {
            Calendar calendar = Calendar.getInstance();
            String strLog = "log" + File.separator + "log_" + calendar.get(Calendar.DAY_OF_MONTH) + "" + calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.HOUR) + "" + calendar.get(Calendar.MINUTE) + ".txt";
            try {
                FilePermission FP = new FilePermission(strLog, "write");
                PrintStream log = new PrintStream(new File(strLog), "ISO-8859-1");
                System.setOut(log);
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "No se puede tener acceso al directorio log\\* \n Permisos Insuficientes", " Error de Escritura", javax.swing.JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }

        new Main();
    }
}
