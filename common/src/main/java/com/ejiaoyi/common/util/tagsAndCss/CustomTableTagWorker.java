package com.ejiaoyi.common.util.tagsAndCss;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.TableTagWorker;
import com.itextpdf.html2pdf.attach.wrapelement.TableRowWrapper;
import com.itextpdf.html2pdf.attach.wrapelement.TableWrapper;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.styledxmlparser.node.IElementNode;

import java.util.Iterator;

/**
 * @author make
 * @since 2020/7/6
 */
public class CustomTableTagWorker extends TableTagWorker {
    private TableWrapper tableWrapper = new TableWrapper();

    public CustomTableTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
    }

    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {

        if (childTagWorker instanceof CustomCaptionTagWorker) {
            TableRowWrapper wrapper = ((CustomCaptionTagWorker) childTagWorker).getTableRowWrapper();
            this.tableWrapper.newRow();

            for (Cell cell : wrapper.getCells()) {
                this.tableWrapper.addCell(cell);
            }

            return true;
        }
        return super.processTagChild(childTagWorker, context);
    }
}
