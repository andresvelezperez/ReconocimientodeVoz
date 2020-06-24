# ReconocimientodeVoz
Reconocimientode de Voz

1. Notas:

Este es un proyecto de reconocimiento de comandos de voz para el idioma español, que consiste en la conversión de voz en texto que representa un comando y su posterior ejecución. Este proyecto vincula varias áreas del conocimiento, como son el reconocimiento de patrones, tratamiento de señales de voz, matemáticas y redes neuronales artificiales.

Desarrollar un interpretador de comandos de voz utilizando redes neuronales artificiales, dando acceso a personas con inhabilidades físicas mejorando la interacción hombre-máquina.

2. Licencia: 
 
Licencia Versión: 1.0 2006 
 
Este software (Icvoz) solo puede ser utilizado si se está de acuerdo con las condiciones de uso establecidas en este documento. Si usted no está de acuerdo con los límites que imponen los términos definidos en este documento, no debe usar el Software, y deberá desinstalar la aplicación (Icvoz). 
 
El usuario que posea esta aplicación, podrá distribuirlo libremente sin costo alguno, sin realizar cambios o modificaciones a los archivos originales de la aplicación, así se incluya el código fuente en el medio que se distribuya esta aplicación.  
 
El usuario poseedor no podrá realizar trabajos dependientes del código (entiéndase como modificación del código), ni tampoco como una librería para sus propias aplicaciones. El usuario no podrá distribuir partes del código (si este es incluido), ni tampoco partes de la aplicación. 
 
Esta licencia aplica para el producto desarrollado por los autores, la licencia para código de terceros, librerías u otras, dependen de sus correspondientes autores. Remitirse a ellas para más información. 
 
Este Software se proporciona "COMO ES", no se dará garantía de ningún tipo, tampoco se garantiza su funcionamiento y si en algún caso el Software produce daños o perjuicios por el mal uso de este o solamente por usarlo, los autores no se hacen responsable en ningún caso por lo sucedido, así se haya informado o no de la posibilidad de ocurrir dicho daño. 
 
El usuario acepta la licencia con tener instalado una copia en el computador, en caso contrario el usuario deberá borrar y eliminar la instalación que posea. 
 
 
 
 
Licencia TERCEROS 
 
Librerías y códigos utilizados en el Interpretador de Comandos de Voz (ICVOZ) con una licencia GNU o compatible.  
 
Audacity is trademarks or registered trademarks of Developers and Contributors. 
(http://audacity.sourceforge.net/) 
FILE: FFT.java 
VER: license-GPL.txt 
 
JDOM is trademarks or registered trademarks of Jason Hunter & Brett McLaughlin (JDOM Project). (http://www.jdom.org/). 
FILE: jdom.jar 
VER: license-JDOM.txt 
 
Network.java (Version 1.0) is released under the limited GNU public license (LGPL). Written in 2002 by Jeff Heaton (http://www.jeffheaton.com). 
FILE: Network.java 
VER: license-LGPL.txt 
 
Sphinx-4 Speech Recognition System is trademarks or registered trademarks of Copyright 1999-2004 Carnegie Mellon University. Portions Copyright 2004 Sun Microsystems, Inc. Portions Copyright 2004 Mitsubishi Electric Research Laboratories.All Rights Reserved.  (http://cmusphinx.sourceforge.net/sphinx4). 
FILE: MelFilter.java, MelFrequencyFilterBank.java, DiscreteCosineTransform 
VER: license.terms 
 
Las licencias mencionadas aquí se encuentran en la carpeta Licencia. 
 
El resto de los productos y nombres de compañías no mencionados aquí pueden ser marcas registradas de sus respectivos propietarios. 
 
 
 
3. Requerimientos: 
 
Un micrófono con supresión de ruido. Referencia aprobada por los autores: Genius HS-04SU. 
120 Mb. De espacio libre en disco. 
512 Mb de memoria Ram. 
Windows XP SP2  (Vista no Comprobado), Linux kernel 2.6 (Kde o Genome). 
1.6 Ghz de velocidad real de procesamiento o superior. 
 
4. Instalación: 
 
Nota: Esta primera versión viene sin instalador. requiere Java 1.6
 
Para instalar copie la carpeta Icvoz que se encuentra en la carpeta de Ejecutable “IcVoz” en “C:\” o su directorio raíz.  Para que el sistema se ejecute cada vez que inicia el computador, cree un acceso directo del script IcVoz.bat en: “Menú Inicio -> Archivos de Programas -> Inicio”. 
 
5. Como usar: 
 
Para iniciar la aplicación se ejecuta el script IcVoz.bat (script de Windows). Luego aparece un icono en la barra del sistema. 
 
 
 
Para visualizar el menú, se da clic derecho sobre el icono de la aplicación que se encuentra en la barra del sistema. 
 

 
En este menú se accede a las opciones de la aplicación. Para conocer a fondo de cómo crear perfiles y modificación de los comandos de voz dirigirse a “Menú => Ayuda”. 
 
Para la utilización en Linux ejecute el jar de la siguiente manera  “java -jar icvoz.jar ” se advierte que algunos comandos no funcionan debido a que se desarrollo para uso inicial en Windows. 
 
6. Uso Avanzado. 
 
Para observar información avanzada sobre la aplicación se dan las siguientes opciones: 
 
DEBUG: Ejecutar el script Debug.bat, para obtener información visual y por consola sobre el estado actual del interpretador de comandos de voz. 
 
VERBOSE: Ejecutar el script Verbose.bat, para obtener información en consola del interpretador de comandos de voz. 
 
EMULACION: Ejecuta IcVoz solo de prueba, no ejecuta ningún comando, es decir, cuando usted pronuncia u comando de Voz, solo aparece un el mensaje de cual comando ejecuto. 
 
Advertencia: 
La aplicación no funciona si se omiten los siguientes directorios: 
 
 lib => contiene las librerías auxiliares. 
 log => carpeta que almacena el registro de funcionamiento. 
 icvoz => archivos de configuración de la aplicación. 
 
La aplicación necesita derechos de escritura para poder operar. 
 
Cualquier error de funcionamiento enviar por correo electrónico los archivos que se encuentren dentro de la carpeta log. 

Changelog:
 
- 20200520
	* Se Convierte a proyecto maven
	* Se ajusta referencias y licencia.

 
Linux Advertencia: Algunos comandos no funcionan debido a que se desarrollo para uso inicial en Windows. 



Java Logo, Java are trademarks or registered trademarks of Sun Microsystems, Inc. in the United States and other countries.