/*
 *	Paquete				: archivo
 *	Archivo				: Archivo.java
 *	Version				: 1.0.0		2007-07-03
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.archivo;

import java.io.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

public class Archivo {

    protected static String confiCarpeta = "icvoz";
    //protected static String wav = "wav.xml";
    protected static String rna = "rna.xml";
    protected static String pss = "pss.xml";

    private String icvozXML = "icvoz.xml";
    private String ruta = null;
    private String archivo = null;

    private Document doc = null;

    public Archivo() throws FileNotFoundException, JDOMException, IOException {
        this.ruta = confiCarpeta + File.separator;
        this.archivo = "icvoz.xml";
        this.init();
    }

    public Archivo(String perfil) throws FileNotFoundException, JDOMException, IOException {
        this.ruta = confiCarpeta + File.separator + perfil + File.separator;
        this.archivo = perfil + ".xml";
        this.init();
    }

    protected Archivo(String perfil, String archivo) throws FileNotFoundException, JDOMException, IOException {
        this.ruta = confiCarpeta + File.separator + perfil + File.separator;
        this.archivo = archivo;
        this.init();
    }

    private void init() throws FileNotFoundException, JDOMException, IOException {
        String file = this.ruta + this.archivo;
        FileInputStream fileInputStream = new FileInputStream(file);
        SAXBuilder builder = new SAXBuilder(false);
        this.doc = builder.build(fileInputStream);
        fileInputStream.close();
    }

    public Document getDocument() {
        return this.doc;
    }

    public void setDocument(Document doc) {
        this.doc = doc;
    }

    public void guardar() throws FileNotFoundException, JDOMException, IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(this.ruta + this.archivo);
        XMLOutputter xMLOutputter = new XMLOutputter();
        xMLOutputter.output(this.doc, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void copiar(String perfil) throws FileNotFoundException, JDOMException, IOException {
        String newRuta = confiCarpeta + File.separator + perfil + File.separator;
        String nombre = this.archivo;
        File file = new File(newRuta);
        file.mkdir();

        Document copiaDoc = (Document) this.doc.clone();

        Element raiz = copiaDoc.getRootElement();
        Element prfl = raiz.getChild("perfil");
        prfl.setText(perfil);

        FileOutputStream fileOutputStream = new FileOutputStream(newRuta + nombre);
        XMLOutputter xMLOutputter = new XMLOutputter();
        xMLOutputter.output(copiaDoc, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void borrar() {
        try {
            String archivo = this.ruta + this.archivo;
            File file = new File(archivo);
            file.delete();
        } catch (Exception e) {
            System.out.println("Error: Archivo:borrar");
        }
    }

    private static void borrarArchivo(File file) {
        try {
            if (file.isDirectory()) {
                File dir[] = file.listFiles();
                for (int i = 0; i < dir.length; i++) {
                    borrarArchivo(dir[i]);
                }
            }
            file.delete();
        } catch (Exception e) {
            System.out.println("Error: Archivo:borrarCarpeta");
        }
    }

    public static void borrarCarpeta(String nombre) {
        String archivo = Archivo.confiCarpeta + File.separator + nombre;
        File file = new File(archivo);
        borrarArchivo(file);
    }

    public static void crearCarpeta(String perfil, String nombre) {
        try {
            String archivo = Archivo.confiCarpeta + File.separator + perfil + File.separator + nombre;
            File file = new File(archivo);
            file.mkdirs();
        } catch (Exception e) {
            System.out.println("Error: Archivo:crearCarpeta");
        }
    }
}
