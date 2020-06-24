/*
 *	Paquete				: archivo
 *	Archivo				: ArchivoCONFIG.java
 *	Version				: 1.0.0		2007-07-04
 *
 *	Todos los derechos reservados.  Usar bajo terminos de la licencia.
 * 
 *	Ver archivo "licencia.txt" para informacion de como usar y
 *	distrubuir este archvio, y para garantizar las responsabilidades 
 *	adquiridas.
 */
package com.avpsoft.icvoz.archivo;

import java.io.*;
import java.util.*;

import org.jdom.*;
import org.jdom.output.*;

public class ArchivoCONFIG extends Archivo {

    public ArchivoCONFIG(String perfil) throws FileNotFoundException, JDOMException, IOException {
        super(perfil);
    }

    public List getComandos() {
        Document doc = (Document) getDocument().clone();
        Element raiz = doc.getRootElement();
        Element comandos = raiz.getChild("comandos");

        List cmd = comandos.getChildren("comando");

        return cmd;
    }

    public void setHabilitado(String codigo, boolean bool) throws FileNotFoundException, JDOMException, IOException {
        Document doc = getDocument();
        Element raiz = doc.getRootElement();
        Element comandos = raiz.getChild("comandos");

        List cmd = comandos.getChildren("comando");

        String aux = "";

        for (int i = 0; i < cmd.size(); i++) {
            if (((Element) cmd.get(i)).getAttributeValue("codigo").compareTo(codigo) == 0) {
                ((Element) cmd.get(i)).getChild("habilitado").setText("" + bool);
                i = cmd.size() + 1;
            }
        }
    }

    /*	private double[] getDouble(String cad)
    	{
    		String dat[]= cad.split("\t");
    		
    		double temp[] = new double[dat.length];
    		
    		for(int i=0; i<temp.length; i++)
    		{
    			temp[i]= Double.valueOf(dat[i]).doubleValue();
    		}
    		
    		return temp;
    	}
    	private String getString(double temp[])
    	{
    		String dat="";
    		
    		for(int i=0; i<temp.length; i++)
    		{
    			dat+= temp[i]+"\t";
    		}
    		return dat;
    	}*/
    public void copiar(String perfil) throws FileNotFoundException, JDOMException, IOException {
        String newRuta = confiCarpeta + File.separator + perfil + File.separator;
        String nombre = perfil + ".xml";
        File file = new File(newRuta);
        file.mkdir();

        Document copiaDoc = (Document) getDocument().clone();

        Element raiz = copiaDoc.getRootElement();
        raiz.setName(perfil);
        Element prfl = raiz.getChild("perfil");
        prfl.setText(perfil);

        FileOutputStream fileOutputStream = new FileOutputStream(newRuta + nombre);
        XMLOutputter xMLOutputter = new XMLOutputter();
        xMLOutputter.output(copiaDoc, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
