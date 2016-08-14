package com.test;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class WorkInThread extends Thread {
    private File xmlFile;
    private File xsdFile;
    private String pathToSuccessXMLDir;
    private String pathToFailedXMLDir;
    private static final Logger logger = Logger.getLogger(WorkInThread.class);

    WorkInThread(File xmlFile, File xsdFile, String pathToSuccessXMLDir, String pathToFailedXMLDir ){
        this.xmlFile = xmlFile;
        this.xsdFile = xsdFile;
        this.pathToSuccessXMLDir = pathToSuccessXMLDir;
        this.pathToFailedXMLDir = pathToFailedXMLDir;
    }
    @Override
    public void run() {
        try {
            File validXmlFile= XmlParserUtils.getValidXml(xmlFile, xsdFile);
            if (validXmlFile==null){
                Files.copy(xmlFile.toPath(), Paths.get(pathToFailedXMLDir+xmlFile.getName()));
                throw new Exception("Bad XML");
            }
            Files.copy(validXmlFile.toPath(), Paths.get(pathToSuccessXMLDir+validXmlFile.getName()));
            putDataToDb(validXmlFile);
        } catch (IOException e) {
            logger.error("Couldn't find path to file"+e.getStackTrace());
        }
        catch (Exception e) {

            logger.error("Bad XML file" + e.getStackTrace() );
        }
    }

    public static void putDataToDb(File validXmlFile){
        Document documentXml = XmlParserUtils.getDocument(validXmlFile);
        String content = XmlParserUtils.getTagContent(documentXml, "content");
        String creationDate = XmlParserUtils.getTagContent(documentXml, "creationDate");
        DbUtils.putToDB(content, creationDate);
    }
}
