/*
 *	Paquete				: gui
 *	Archivo				: Configuracion.java
 *	Version				: 1.0.0		2007-07-05
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.gui;

import com.avpsoft.icvoz.archivo.Archivo;
import com.avpsoft.icvoz.archivo.ArchivoCONFIG;
import com.avpsoft.icvoz.archivo.ArchivoPSS;
import com.avpsoft.icvoz.archivo.ArchivoRNA;
import com.avpsoft.icvoz.main.Main;
import javax.help.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

import org.jdom.*;

public class Configuracion extends JFrame {

    private JFrame miVentana;
    private Archivo config;
    private MyTable myTable;

    private ConfiguracionPerfil configPerfil;
    private JScrollPane scrollTable;
    public JPanel target, principal;

    public static ImageIcon imgFondo375x40 = new javax.swing.ImageIcon(Configuracion.class.getClass().getResource("/img/fondo375x40.png"));
    public static ImageIcon imgFondo375x50 = new javax.swing.ImageIcon(Configuracion.class.getClass().getResource("/img/fondo375x50.png"));

    public Configuracion() {
        super("Asistente de Configuracion");
        setResizable(false);
        setLayout(new BorderLayout());
        Image image = new javax.swing.ImageIcon(getClass().getResource("/img/icvoz32x32.png")).getImage();
        setIconImage(image);

        ImageIcon imgLateral = new javax.swing.ImageIcon(Configuracion.class.getClass().getResource("/img/lateral.png"));
        JLabel lateral = new JLabel(imgLateral);
        lateral.setPreferredSize(new Dimension(100, 395));
        lateral.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        ImageIcon imgEdicion = new javax.swing.ImageIcon(getClass().getResource("/img/personal.png"));
        JLabel titulo = new JLabel("Perfiles de Usaurio", imgEdicion, JLabel.CENTER);

        JLabel superior = new JLabel(imgFondo375x50);
        superior.setLayout(new BorderLayout());
        superior.setPreferredSize(new Dimension(375, 50));
        superior.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        superior.add(titulo, BorderLayout.CENTER);

        myTable = new MyTable();
        scrollTable = new JScrollPane();
        scrollTable.setPreferredSize(new Dimension(375, 216));
        scrollTable.getViewport().add(myTable);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setPreferredSize(new Dimension(375, 260));
        centro.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        centro.add(scrollTable, BorderLayout.NORTH);

        ImageIcon imgBorrar = new javax.swing.ImageIcon(getClass().getResource("/img/editdelete.png"));
        ImageIcon imgNuevo = new javax.swing.ImageIcon(getClass().getResource("/img/filenew.png"));
        ImageIcon imgAyuda = new javax.swing.ImageIcon(getClass().getResource("/img/help.png"));
        ImageIcon imgCancelar = new javax.swing.ImageIcon(getClass().getResource("/img/no.png"));
        ImageIcon imgAceptar = new javax.swing.ImageIcon(getClass().getResource("/img/ok.png"));
        ImageIcon imgAtras = new javax.swing.ImageIcon(getClass().getResource("/img/back.png"));

        JButton nuevo = new JButton(imgNuevo);
        nuevo.setToolTipText("Nuevo Perfil");
        nuevo.setHorizontalTextPosition(JButton.LEFT);
        nuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Object[] options = {"SI", "NO"};
                boolean repetido = false;
                int op = JOptionPane.showOptionDialog(null, "Desea crear un nuevo perfil?", "Nuevo Perfil",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (op == 0) {
                    String nuevoNombre = "";
                    do {
                        repetido = false;
                        nuevoNombre = JOptionPane.showInputDialog(null, "Por favor ingrese el nombre del perfil");
                        if (nuevoNombre == null) {
                            break;
                        }
                        Object nombrePerfiles[] = getNombrePerfiles(false);
                        for (int i = 0; i < nombrePerfiles.length; i++) {
                            if (nuevoNombre.compareTo((String) nombrePerfiles[i]) == 0) {
                                repetido = true;
                                break;
                            }
                        }
                        if (repetido) {
                            JOptionPane.showMessageDialog(null, "Debe ingresar un perfil diferente a los actuales", "Perfil", JOptionPane.PLAIN_MESSAGE);
                        }
                        if (nuevoNombre.contains(String.valueOf(' ').subSequence(0, 1))) {
                            JOptionPane.showMessageDialog(null, "El nombre del perfil no pude tener espacios en blanco", "Perfil", JOptionPane.PLAIN_MESSAGE);
                        }
                    } while (nuevoNombre.length() < 3 || nuevoNombre.contains(String.valueOf(' ').subSequence(0, 1)) || repetido);
                    if (nuevoNombre != null) {
                        nuevoPerfil(nuevoNombre);
                    }
                }
            }
        });
        JButton borrar = new JButton(imgBorrar);
        borrar.setToolTipText("Borrar Perfil");
        borrar.setHorizontalTextPosition(JButton.LEFT);
        borrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Object[] possibleValues = getNombrePerfiles();
                if (possibleValues.length > 0) {
                    Object selectedValue = JOptionPane.showInputDialog(null,
                            "Seleccione el perfil a borrar", "Borrar Perfil",
                            JOptionPane.WARNING_MESSAGE, null,
                            possibleValues, possibleValues[0]);
                    if (selectedValue != null) {
                        Object[] options = {"SI", "NO"};
                        int op = JOptionPane.showOptionDialog(null, "Desea borrar el perfil, " + selectedValue + " ?", "Borrar Perfil",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                        if (op == 0) {
                            borrarPerfil((String) selectedValue);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontraron elementos para ser borrados", "Borrar Perfil", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JLabel botonesCentro = new JLabel(imgFondo375x40);
        botonesCentro.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botonesCentro.setPreferredSize(new Dimension(375, 40));
        botonesCentro.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        botonesCentro.add(nuevo);
        botonesCentro.add(borrar);
        centro.add(botonesCentro, BorderLayout.CENTER);

        JButton atras = new JButton(imgAtras);
        atras.setToolTipText("Atras");
        atras.setHorizontalTextPosition(JButton.LEFT);
        atras.setEnabled(false);

        JButton aplicar = new JButton(imgAceptar);
        aplicar.setToolTipText("Aceptar");
        aplicar.setHorizontalTextPosition(JButton.LEFT);
        aplicar.setEnabled(false);

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
        CSH.setHelpIDString(ayuda, "perfil.descripcion");
        ayuda.addActionListener(new CSH.DisplayHelpFromSource(Main.helpBroker));

        JLabel botones = new JLabel(imgFondo375x40);
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setPreferredSize(new Dimension(375, 40));
        botones.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        botones.add(atras);
        botones.add(aplicar);
        botones.add(cancelar);
        botones.add(ayuda);

        principal = new JPanel(new BorderLayout());
        principal.add(superior, BorderLayout.NORTH);
        principal.add(centro, BorderLayout.CENTER);
        principal.add(botones, BorderLayout.SOUTH);

        target = new JPanel();
        target.setBackground(new Color(0xf6, 0xf6, 0xf6));
        target.add(principal);

        getContentPane().add(lateral, BorderLayout.WEST);
        getContentPane().add(target, BorderLayout.CENTER);

        this.miVentana = this;

        setPreferredSize(new Dimension(500, 395));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 726) / 2, (screenSize.height - 395) / 2, 726, 395);
    }

    public Object[] getNombrePerfiles() {
        return getNombrePerfiles(true);
    }

    private Object[] getNombrePerfiles(boolean filtro) {
        Document doc = config.getDocument();
        Element raiz = doc.getRootElement();
        Element usuario = raiz.getChild("perfil");
        String nombreUsaurio = usuario.getText();
        Element comandos = raiz.getChild("perfiles");

        java.util.List cmd = comandos.getChildren("perfil");
        Vector<String> nombres = new Vector<String>(0, 1);

        for (int i = 0; i < cmd.size(); i++) {
            if (filtro) {
                if (!(((Element) cmd.get(i)).getChild("nombre").getText().compareTo(nombreUsaurio) == 0) && !(((Element) cmd.get(i)).getChild("nombre").getText().compareTo("default") == 0)) {
                    nombres.addElement(((Element) cmd.get(i)).getChild("nombre").getText());
                }
            } else {
                nombres.addElement(((Element) cmd.get(i)).getChild("nombre").getText());
            }
        }

        return nombres.toArray();
    }

    private class MyTable extends JPanel {

        private JCheckBox activo[];
        private java.util.List cmd;
        private Element usuario;
        private int posUsu = 0;

        public MyTable() {
            super(new SpringLayout());
            try {
                init();
            } catch (Exception e) {
                System.out.println("Error: configurar:constructor");
            }
        }

        private void init() throws FileNotFoundException, JDOMException, IOException {
            ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/img/configure.png"));

            config = new Archivo();

            Document doc = config.getDocument();
            Element raiz = doc.getRootElement();
            usuario = raiz.getChild("perfil");
            String nombreUsaurio = usuario.getText();
            Element comandos = raiz.getChild("perfiles");

            cmd = comandos.getChildren("perfil");

            activo = new JCheckBox[cmd.size()];
            ButtonGroup buttonGroup = new ButtonGroup();

            add(new JLabel("Activo", JLabel.CENTER));
            add(new JLabel("Perfil", JLabel.CENTER));
            add(new JLabel("Configurar", JLabel.CENTER));

            for (int i = 0; i < cmd.size(); i++) {
                final int c = i;
                activo[i] = new JCheckBox();
                activo[i].setSelected(new Boolean(((Element) cmd.get(i)).getChild("nombre").getText().compareTo(nombreUsaurio) == 0));
                if (activo[i].isSelected()) {
                    posUsu = i;
                }
                //activo[i].setEnabled(false);
                buttonGroup.add(activo[i]);
                JPanel checkbox = new JPanel();
                checkbox.add(activo[i]);
                add(checkbox);
                activo[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        habilitarPerfil(((Element) cmd.get(c)).getChild("nombre"), c);
                    }
                });
                JPanel label = new JPanel();
                label.add(new JLabel(((Element) cmd.get(i)).getChild("nombre").getText()));
                add(label);

                JButton accion = new JButton(img);
                accion.setEnabled(((Element) cmd.get(i)).getChild("nombre").getText().compareTo("default") == 0 ? false : true);
                accion.setToolTipText("Configurar");
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
            //Lay out the panel.
            SpringUtilities.makeGrid(this,
                    cmd.size() + 1, 3, //rows, cols
                    5, 5, //initialX, initialY
                    5, 5);//xPad, yPad
        }

        public void habilitarPerfil(Element e, int i) {
            if (usuario != null) {
                Object[] options = {"SI", "NO"};
                int op = JOptionPane.showOptionDialog(null, "Ha cambido el perfil actual, desea cambiarlo", "Perfil",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

                if (op == 0) {
                    usuario.setText(e.getText());
                    posUsu = i;
                    try {
                        config.guardar();
                    } catch (Exception ex) {
                        System.out.println("Error: Configurar:habilitarPerfil");
                    }
                } else {
                    if (activo.length > posUsu) {
                        activo[posUsu].setSelected(true);
                    } else {
                        activo[0].setSelected(true);
                    }
                }
            }
        }
    }

    public void configurar(Element e, int i) {
        System.out.println(i + " ==> " + e.getChild("nombre").getText());

        configPerfil = new ConfiguracionPerfil(e.getChild("nombre").getText(), this.target, this);

        target.removeAll();
        target.add(configPerfil);
        target.repaint();
        configPerfil.repaint();
        repaint();
        target.revalidate();
    }

    public void inicio() {
        target.removeAll();
        target.add(principal);
        repaint();
    }

    public void setVisible(boolean bool) {
        super.setVisible(bool);
        bienvenido();
    }

    public void finalizar() {
        JPanel finalizar = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("Interpretador de Comandos de Voz", JLabel.CENTER);

        JLabel superior = new JLabel(imgFondo375x50);
        superior.setLayout(new BorderLayout());
        superior.setPreferredSize(new Dimension(375, 50));
        superior.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        superior.add(titulo, BorderLayout.CENTER);

        JLabel label = new JLabel("Ha finalizado el asistente", JLabel.CENTER);

        ImageIcon imgCfondo = new javax.swing.ImageIcon(getClass().getResource("/img/fondoCentro.png"));
        JLabel centro = new JLabel(imgCfondo);
        centro.setLayout(new BorderLayout());
        centro.setPreferredSize(new Dimension(375, 260));
        centro.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        centro.add(label, BorderLayout.CENTER);

        ImageIcon imgSigui = new javax.swing.ImageIcon(getClass().getResource("/img/ok.png"));
        ImageIcon imgAyuda = new javax.swing.ImageIcon(getClass().getResource("/img/help.png"));
        ImageIcon imgCancelar = new javax.swing.ImageIcon(getClass().getResource("/img/no.png"));
        ImageIcon imgAtras = new javax.swing.ImageIcon(getClass().getResource("/img/back.png"));

        JButton atras = new JButton(imgAtras);
        atras.setToolTipText("Atras");
        atras.setHorizontalTextPosition(JButton.LEFT);
        atras.setEnabled(false);

        JButton sigui = new JButton(imgSigui);
        sigui.setToolTipText("Finalizar");
        sigui.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
            }
        });

        JButton cancelar = new JButton(imgCancelar);
        cancelar.setToolTipText("Cancelar");
        cancelar.setHorizontalTextPosition(JButton.LEFT);
        cancelar.setEnabled(false);

        JButton ayuda = new JButton(imgAyuda);
        ayuda.setToolTipText("Ayuda");
        ayuda.setHorizontalTextPosition(JButton.LEFT);
        ayuda.setEnabled(false);

        JLabel botones = new JLabel(imgFondo375x40);
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setPreferredSize(new Dimension(375, 40));
        botones.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        botones.add(atras);
        botones.add(sigui);
        botones.add(cancelar);
        botones.add(ayuda);

        finalizar.add(superior, BorderLayout.NORTH);
        finalizar.add(centro, BorderLayout.CENTER);
        finalizar.add(botones, BorderLayout.SOUTH);

        target.removeAll();
        target.add(finalizar);
        repaint();
        target.revalidate();
    }

    public void bienvenido() {
        JPanel bienvenido = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("Interpretador de Comandos de Voz", JLabel.CENTER);

        JLabel superior = new JLabel(imgFondo375x50);
        superior.setLayout(new BorderLayout());
        superior.setPreferredSize(new Dimension(375, 50));
        superior.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        superior.add(titulo, BorderLayout.CENTER);

        ImageIcon imgCfondo = new javax.swing.ImageIcon(getClass().getResource("/img/fondoCentro.png"));
        JLabel label = new JLabel("<html>"
                + "<head>"
                + "<title></title>"
                + "</head>"
                + "<p>&nbsp;</p>"
                + "<body>"
                + "<p>&nbsp;</p>"
                + "<p><font size=\"+1\"><em>Bienvenidos...</em></font></p>"
                + "<hr size=\"2\">"
                + "<p>&nbsp;</p>"
                + "<p>Este asistente lo guiara </p>"
                + "<p>atravez de la configuraci&oacute;n</p>"
                + "<p>del sistema.</p>"
                + "</body>"
                + "</html>", JLabel.CENTER);

        JLabel centro = new JLabel(imgCfondo);
        centro.setLayout(new BorderLayout());
        centro.setPreferredSize(new Dimension(375, 260));
        centro.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        centro.add(label, BorderLayout.CENTER);

        ImageIcon imgSigui = new javax.swing.ImageIcon(getClass().getResource("/img/forward.png"));
        //ImageIcon imgSigui= new javax.swing.ImageIcon(getClass().getResource("/img/ok.png"));
        ImageIcon imgAyuda = new javax.swing.ImageIcon(getClass().getResource("/img/help.png"));
        ImageIcon imgCancelar = new javax.swing.ImageIcon(getClass().getResource("/img/no.png"));
        ImageIcon imgAtras = new javax.swing.ImageIcon(getClass().getResource("/img/back.png"));

        JButton atras = new JButton(imgAtras);
        atras.setToolTipText("Atras");
        atras.setHorizontalTextPosition(JButton.LEFT);
        atras.setEnabled(false);

        JButton sigui = new JButton(imgSigui);
        sigui.setToolTipText("Siguiente");
        sigui.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inicio();
            }
        });

        JButton cancelar = new JButton(imgCancelar);
        cancelar.setToolTipText("Cancelar");
        cancelar.setHorizontalTextPosition(JButton.LEFT);
        cancelar.setEnabled(false);

        JButton ayuda = new JButton(imgAyuda);
        ayuda.setToolTipText("Ayuda");
        ayuda.setHorizontalTextPosition(JButton.LEFT);
        ayuda.setEnabled(false);

        JLabel botones = new JLabel(imgFondo375x40);
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setPreferredSize(new Dimension(375, 40));
        botones.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        botones.add(atras);
        botones.add(sigui);
        botones.add(cancelar);
        botones.add(ayuda);

        bienvenido.add(superior, BorderLayout.NORTH);
        bienvenido.add(centro, BorderLayout.CENTER);
        bienvenido.add(botones, BorderLayout.SOUTH);

        target.removeAll();
        target.add(bienvenido);
        repaint();
        target.revalidate();
    }

    private void borrarPerfil(String nombre) {
        Document doc = config.getDocument();
        Element raiz = doc.getRootElement();
        Element perfiles = raiz.getChild("perfiles");
        java.util.List perfil = perfiles.getChildren("perfil");

        Element aux = null;

        for (int i = 0; i < perfil.size(); i++) {
            if (((Element) perfil.get(i)).getChild("nombre").getText().compareTo(nombre) == 0) {
                aux = (Element) perfil.get(i);
                i = perfil.size() + 1;
            }
        }

        boolean comprobacion = perfiles.removeContent(aux);

        try {
            config.guardar();
            new ArchivoCONFIG(nombre).borrar();
            new ArchivoPSS(nombre).borrar();
            new ArchivoRNA(nombre).borrar();
            Archivo.borrarCarpeta(nombre);

            myTable = null;
            myTable = new MyTable();

            scrollTable.getViewport().removeAll();
            scrollTable.getViewport().add(myTable);

            repaint();
        } catch (Exception e) {
            System.out.println("Error: Configuracion:borrarPerfil");
        }
    }

    private void nuevoPerfil(String nombre) {
        Document doc = config.getDocument();
        Element raiz = doc.getRootElement();

        Element comandos = raiz.getChild("perfiles");

        Element perfil = new Element("perfil");

        Element usu = new Element("nombre");

        usu.setText(nombre);

        Element editable = new Element("editable");
        editable.setText("" + true);

        perfil.addContent(usu);
        perfil.addContent(editable);
        comandos.addContent(perfil);
        try {
            new ArchivoCONFIG("default").copiar(nombre);
            new ArchivoPSS("default").copiar(nombre);
            new ArchivoRNA("default").copiar(nombre);
            Archivo.crearCarpeta(nombre, "wav");
            config.guardar();

            myTable = null;
            myTable = new MyTable();

            scrollTable.getViewport().removeAll();
            scrollTable.getViewport().add(myTable);

            repaint();

        } catch (Exception e) {
            System.out.println("Error: Configuracion: nuevoPerfil");
            e.printStackTrace();
        }
    }
}
