package com.test;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;


public class XmlParserUtils {

    private static final Logger logger = Logger.getLogger(XmlParserUtils.class);

    public static File getValidXml(File fileXml, File fileXsd){
        if(validateXMLSchema(fileXml, fileXsd) && validateContentLength(fileXml)){
            return fileXml;
        }
        return null;
    }

    public static String getTagContent(Document documentXml, String tag) {
        return documentXml.getElementsByTagName(tag).item(0).getTextContent();
    }

    public static Document getDocument(File fileXml){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(fileXml);
        }catch (Exception e){
            logger.error("Can't parse xml file "+e.getStackTrace());
        }
        return null;
    }

    private static boolean validateContentLength(File fileXml) {
        Document documentXml = getDocument(fileXml);
        if (documentXml == null)
            return false;
        NodeList content = documentXml.getElementsByTagName("content");
        Node item = content.item(0);
        int contentLength=item.getTextContent().length();
        if (contentLength<=1024){
            return true;
        }
        else {
            logger.error("Content too long");
            return false;
        }
    }

    public static boolean validateXMLSchema(File fileXml, File fileXsd){
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(fileXsd);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(fileXml));
        } catch (IOException e){
            logger.error("File not found " + e.getStackTrace());
            return false;
        }catch(SAXException e){
            logger.error("Wrong XML " + e.getStackTrace());
            return false;
        }
        return true;
    }
}
