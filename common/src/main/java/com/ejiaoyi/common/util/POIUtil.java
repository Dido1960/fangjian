package com.ejiaoyi.common.util;

import com.ejiaoyi.common.constant.HtmlLabelConstant;
import fr.opensagres.poi.xwpf.converter.core.ImageManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 转换工具类
 * @author Make
 * @since 2020/7/24
 */
public class POIUtil {
    private static String imagePath = "htmlImage";
    private static String resPath = "";

    /*
     * docx转html
     *
     * @param src
     * @param dest
     */
    public static void docx2Html(String src, String dest) {
        String resPath = src.substring(0, dest.lastIndexOf("/"));
        String temp = dest.replace(".html", "_temp.html");
        OutputStreamWriter outputStreamWriter = null;
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(src));
            // 输出选项设置
            XHTMLOptions options = XHTMLOptions.create();
            // 图片路径设置
            ImageManager im = new ImageManager(new File(resPath), imagePath);
            options.setImageManager(im);
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(temp), StandardCharsets.UTF_8);
            // 转换器
            XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, outputStreamWriter, options);
            // 读取转换好的临时html文件并改变其样式将其写入新的文件中
            String content = splitContext(temp);
            writeFile(changeHtmlStyle(content), dest);
            // 删除临时文件
            File file = new File(temp);
            FileUtil.deleteFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * docx转html,直接获取content
     *
     * @param src
     * @param dest
     */
    public static String docx2HtmlText(String src, String dest) {
        String resPath = new File(dest).getParent();
        FileUtil.createDir(dest);
        OutputStreamWriter outputStreamWriter = null;
        XWPFDocument document = null;
        try {
            document = new XWPFDocument(new FileInputStream(src));
            // 输出选项设置
            XHTMLOptions options = XHTMLOptions.create();
            // 图片路径设置
            ImageManager im = new ImageManager(new File(resPath), imagePath);
            options.setImageManager(im);
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(dest), StandardCharsets.UTF_8);
            // 转换器
            XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, outputStreamWriter, options);
            // 读取转换好的临时html文件并改变其样式将其写入新的文件中
            String content = splitContext(dest);
            // 删除临时文件
            File file = new File(dest);
            FileUtil.deleteFile(file);
            file = new File(resPath + "/" + imagePath);
            FileUtil.deleteFile(file);
            return changeHtmlStyle(content);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * doc转html(word97-2003)
     *
     * @param src
     * @param dest
     */
    @Deprecated
    public static void doc2Html(String src, String dest) {
        HWPFDocument wordDocument;
        resPath = src.substring(0, dest.lastIndexOf("/") + 1) + imagePath;
        FileUtil.createDir(resPath);
        // 字节码输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            // 根据输入文件路径与名称读取文件流
            InputStream in = new FileInputStream(src);
            // 把文件流转化为输入wordDom对象
            wordDocument = new HWPFDocument(in);
            // 通过反射构建dom创建者工厂
            DocumentBuilderFactory domBuilderFactory = DocumentBuilderFactory.newInstance();
            // 生成dom创建者
            DocumentBuilder domBuilder = domBuilderFactory.newDocumentBuilder();
            // 生成dom对象
            Document dom = domBuilder.newDocument();
            // 生成针对Dom对象的转化器
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(dom);
            // 转化器重写内部方法
            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                @Override
                public String savePicture(byte[] content,
                                          PictureType pictureType, String name,
                                          float widthInches, float heightInches) {
                    try {
                        FileOutputStream out = new FileOutputStream(new File(resPath + "/" + name));
                        out.write(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return resPath + "/" + name;
                }
            });
            // 转化器开始转化接收到的dom对象
            wordToHtmlConverter.processDocument(wordDocument);

            // 从加载了输入文件中的转换器中提取DOM节点
            Document htmlDocument = wordToHtmlConverter.getDocument();
            // 从提取的DOM节点中获得内容
            DOMSource domSource = new DOMSource(htmlDocument);

            // 输出流的源头
            StreamResult streamResult = new StreamResult(out);
            // 转化工厂生成序列转化器
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            // 设置序列化内容格式
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");

            serializer.transform(domSource, streamResult);
            // 生成文件方法
            writeFile(changeDocHtmlStyle(new String(out.toByteArray())), dest);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 输出文件
     *
     * @param content
     * @param path
     */
    private static void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            bw.write(content);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    /*
     * 读取转换得到的html文件，并过滤多余空行
     *
     * @param filePath
     * @return
     */
    public static String splitContext(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                if (!"".equals(tempString)) {
                    sb.append("\n");
                }
            }
            reader.close();
            return sb.toString().replaceAll("\\n+", "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return "";
    }

    /*
     * 修改html的样式(doc用)
     *
     * @param content
     * @return
     */
    private static String changeDocHtmlStyle(String content) {
        org.jsoup.nodes.Document document = Jsoup.parse(content);
        Elements tds = document.select(HtmlLabelConstant.LABEL_TABLE_TD);
        for (Element td : tds) {
            if (!td.hasText()) {
                td.text(" ");
            }
        }
        //添加整体样式
        return document.html();
    }

    /*
     * 修改html的样式(docx用)
     *
     * @param content
     * @return
     */
    private static String changeHtmlStyle(String content) {
        org.jsoup.nodes.Document document = Jsoup.parse(content);
        //添加整体样式
        document.select(HtmlLabelConstant.LABEL_STYLE).append("td { border: 1px solid #e7e7e7;font-size: 14px;padding: 10px 0 10px 5px;}");
        Elements divs = document.select(HtmlLabelConstant.LABEL_DIV);
        Elements tables = document.select(HtmlLabelConstant.LABEL_TABLE);
        Elements tds = document.select(HtmlLabelConstant.LABEL_TABLE_TD);
        Elements spans = document.select(HtmlLabelConstant.LABEL_SPAN);
        Elements imgs = document.select(HtmlLabelConstant.LABEL_IMG);
        for (Element div : divs) {
            div = changeElementsStyle(div);
        }
        for (Element table : tables) {
            table = changeElementsStyle(table);
        }
        for (Element td : tds) {
            td = changeElementsStyle(td);
        }
        for (Element span : spans) {
            span = changeElementsStyle(span);
        }
        for (Element img : imgs) {
            Element ele = img.nextElementSibling();
            if (ele != null && !HtmlLabelConstant.LABEL_IMG.equals(ele.tagName())) {
                img.after(HtmlLabelConstant.LABEL_BR);
            }
        }
        return document.html();
    }

    /*
     * 修改节点的样式(docx用)
     *
     * @param element
     * @return
     */
    private static Element changeElementsStyle(Element element) {
        String tagName = element.tagName();
        String style = element.attr(HtmlLabelConstant.LABEL_STYLE);
        // 修改td标签的样式
        if (HtmlLabelConstant.LABEL_TABLE_TD.equals(tagName)) {
            String reg = "(\\bwidth:\\b)+([1-9]\\d*|0)(\\.\\d{1,2})?pt;";
            String width = "";
            Matcher matcher = Pattern.compile(reg).matcher(style);
            if (matcher.find()) {
                width = matcher.group();
            }
            element.attr(HtmlLabelConstant.LABEL_STYLE, width);
            // 防止表格因没有值而发生变形
            if (element.child(0) != null) {
                if (!element.child(0).hasText()) {
                    element.child(0).text(" ");
                }
            }
            return element;
        }
        // 修改table与div的样式
        String reg = "(\\bwidth:\\b|\\bmargin-right:\\b|\\bmargin-left:\\b)+([1-9]\\d*|0)(\\.\\d{1,2})?pt;";
        style = style.replaceAll(reg, "");
        style = style + "width: 98%;margin: auto;";
        // table的额外样式
        if (HtmlLabelConstant.LABEL_TABLE.equals(tagName)) {
            style = style + "border-collapse : collapse;border-spacing : 0;border: 0 none;";
        }
        element.attr(HtmlLabelConstant.LABEL_STYLE, style);
        return element;
    }

}
