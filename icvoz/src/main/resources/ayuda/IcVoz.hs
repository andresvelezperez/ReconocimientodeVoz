<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN"
         "../dtd/helpset_2_0.dtd">

<helpset version="1.0">

  <!-- title -->
  <title>Ayuda - IcVoz</title>

  <!-- maps -->
  <maps>
     <homeID>top</homeID>
     <mapref location="Map.jhm"/>
  </maps>

  <!-- views -->
  <view>
    <name>TOC</name>
    <label>Tabla de Contenido</label>
    <type>javax.help.TOCView</type>
    <data>TOC.xml</data>
  </view>

  <view>
    <name>Index</name>
    <label>Indice</label>
    <type>javax.help.IndexView</type>
    <data>Index.xml</data>
  </view>

  <!--view>
    <name>Busqueda</name>
    <label>Search</label>
    <type>javax.help.SearchView</type>
    <data engine="com.sun.java.help.search.DefaultSearchEngine">
      JavaHelpSearch
    </data>
  </view-->
<presentation default="true" displayviewimages="false">
     <name>Ventana Icvoz</name>
     <size width="700" height="400" />
     <location x="200" y="200" />
     <title>Ayuda - IcVoz</title>
     <image>toplevelfolder</image>
     <toolbar>
	<helpaction>javax.help.BackAction</helpaction>
	<helpaction>javax.help.ForwardAction</helpaction>
	<helpaction>javax.help.SeparatorAction</helpaction>
	<helpaction>javax.help.HomeAction</helpaction>
	<helpaction>javax.help.ReloadAction</helpaction>
	<helpaction>javax.help.SeparatorAction</helpaction>
	<helpaction>javax.help.PrintAction</helpaction>
	<helpaction>javax.help.PrintSetupAction</helpaction>
     </toolbar>
  </presentation>
  <presentation>
     <name>IcVoz</name>
     <size width="400" height="400" />
     <location x="200" y="200" />
     <title>Ayuda - IcVoz</title>
  </presentation>
</helpset>
