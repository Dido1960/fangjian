package com.ejiaoyi.common.util;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;

/**
 * XSD工具类
 *
 * @author Z0001
 * @since 2020-4-26
 */
@Slf4j
public class XSDUtil {

    /**
     * 通过XSD输入流 检测XML文件格式
     *
     * @param xsdIs   xsd文件输入流
     * @param xmlPath xml文件路径
     * @return XML文件格式是否正确
     * @throws SAXException Sax异常
     * @throws IOException  IO流异常
     */
    public static boolean validXML(InputStream xsdIs, String xmlPath) throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Source xsdSource = new StreamSource(xsdIs);
        Schema schema = schemaFactory.newSchema(xsdSource);
        Validator validator = schema.newValidator();
        Source source = new StreamSource(xmlPath);
        try {
            validator.validate(source);
        } catch (Exception e) {
            log.error("xsd util valid xml error : " + e.getMessage());
            return false;
        }

        return true;
    }
}
