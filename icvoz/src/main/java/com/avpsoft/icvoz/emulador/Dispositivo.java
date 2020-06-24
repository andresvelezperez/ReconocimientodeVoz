/*
 *	Paquete				: emulador
 *	Archivo				: Dispositivo.java
 *	Version				: 1.0.0		2007-08-01
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.emulador;

import com.avpsoft.icvoz.archivo.*;
import com.avpsoft.icvoz.main.Main;

import org.jdom.*;

public class Dispositivo {

    private String perfil;
    private ArchivoCONFIG archivo = null;
    private java.util.List datos = null;
    private Class key = null;
    private java.awt.Robot device = null;

    public Dispositivo(String perfil) {
        this.perfil = perfil;

        try {
            //Inicializa el archivo de configuracion
            this.archivo = new ArchivoCONFIG(this.perfil);
            //Se obtienen los comandos
            this.datos = archivo.getComandos();
            //Se iniciliza la clase que contiene los codigos del teclado
            key = Class.forName("java.awt.event.KeyEvent");
            //Se inicializa la clase que emulara el teclado
            device = new java.awt.Robot();

        } catch (Exception e) {
            System.out.println("Error: Dispositivo:Constructor");
        }
    }

    private Element getComando(String id) {
        if (id.length() < 2) {
            id = "0" + id;
        }

        id = "icvoz" + id;

        if (datos == null) {
            return null;
        }

        for (int i = 0; i < datos.size(); i++) {
            if (((Element) datos.get(i)).getAttributeValue("codigo").compareTo(id) == 0) {
                Element habilitado = ((Element) datos.get(i)).getChild("habilitado");
                if (habilitado != null) {
                    boolean estado = Boolean.valueOf(habilitado.getText()).booleanValue();
                    if (!estado) {
                        Main.mensageInfo("( ( Voz ) )", "Comando desabilitado: " + ((Element) datos.get(i)).getAttributeValue("nombre"));
                    } else {
                        return (Element) datos.get(i);
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /*
     * Para ejecutar una tecla se realiza la operacion de:
     *	keyPress		(Presion)
     *	keyRelease		(Suelta)
     */
    public void eventoTeclado(String id) {
        //Se busca el comando
        Element cmd = getComando(id);

        //Se realiza la condicion de que exista.
        if (cmd == null) {
            System.out.println("Error: Comando no Encontrado");
            return;
        }
        //Se obtiene el moficador "Ctrl", "Alt"...
        String modificador = cmd.getAttributeValue("modificador");
        //Se obtiene la tecla "A", "Enter"...
        String tecla = cmd.getAttributeValue("tecla");
        //Se realiza la validacion de que se haya inicilizado la clase
        //	- java.awt.Robot
        //	- java.awt.event.KeyEvent
        if (this.key == null || this.device == null) {
            System.out.println("Error: Controlador de teclado no encontrado");
            return;
        }
        // Se valida de que la tecla exista
        if (tecla == null && modificador == null) {
            return;
        }

        if (tecla == null) {
            return;
        }

        if (Main.EMULACION) {
            try {
                //Se procede a covertir el modificador en entero
                // y ejecutar la tecla (presionar)
                if (modificador != null) {
                    device.keyPress(key.getField(modificador).getInt(key));
                }

                //Se procede a covertir la tecla en entero
                // y ejecutar la tecla (presionar)
                if (tecla != null) {
                    device.keyPress(key.getField(tecla).getInt(key));
                }

                //Se procede a covertir el modificador en entero
                // y ejecutar la tecla (soltar)
                if (modificador != null) {
                    device.keyRelease(key.getField(modificador).getInt(key));
                }

                //Se procede a covertir la tecla en entero
                // y ejecutar la tecla	(soltar)
                if (tecla != null) {
                    device.keyRelease(key.getField(tecla).getInt(key));
                }

            } catch (Exception e) {
                System.out.println("Error: Dispositivo:eventoTeclado \n" + e);
            }
        }
        //Se obtiene el nombre del comando.
        String nombre = cmd.getAttributeValue("nombre");
        //Se notifica al usuario el comando ejecutado
        Main.mensageInfo("( ( Voz ) )", "Se ha ejecutado: " + nombre);
    }
}
