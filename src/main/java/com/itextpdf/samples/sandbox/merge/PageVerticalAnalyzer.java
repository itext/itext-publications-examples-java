package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Path;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Subpath;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.data.PathRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This rendering {@link IEventListener} looks for vertical <em>sections of use</em> on a page.
 * After parsing a page it share a list of floats which contains the border y coordinates
 * between used (drawn onto) and unused vertical sections.
 *
 * @author mkl
 */
public class PageVerticalAnalyzer implements IEventListener {

    final Set<EventType> supportedEvents;
    final List<Float> verticalFlips = new ArrayList<>();

    public PageVerticalAnalyzer() {
        supportedEvents = new HashSet<>();
        supportedEvents.add(EventType.RENDER_TEXT);
        supportedEvents.add(EventType.RENDER_PATH);
        supportedEvents.add(EventType.RENDER_IMAGE);
    }

    public List<Float> getVerticalFlips() {
        return verticalFlips;
    }

    //
    // EventListener implementation
    // 
    @Override
    public void eventOccurred(IEventData data, EventType type) {
        switch (type) {
            case RENDER_IMAGE: {
                ImageRenderInfo renderInfo = (ImageRenderInfo) data;
                Matrix ctm = renderInfo.getImageCtm();
                float[] yCoords = new float[4];
                for (int x = 0; x < 2; x++)
                    for (int y = 0; y < 2; y++) {
                        Vector corner = new Vector(x, y, 1).cross(ctm);
                        yCoords[2 * x + y] = corner.get(Vector.I2);
                    }
                Arrays.sort(yCoords);
                addVerticalUseSection(yCoords[0], yCoords[3]);
                break;
            }
            case RENDER_PATH: {
                PathRenderInfo renderInfo = (PathRenderInfo) data;
                if (renderInfo.getOperation() != PathRenderInfo.NO_OP) {
                    Matrix ctm = renderInfo.getCtm();
                    Path path = renderInfo.getPath();
                    for (Subpath subpath : path.getSubpaths()) {
                        List<Float> yCoordsList = new ArrayList<>();
                        for (Point point2d : subpath.getPiecewiseLinearApproximation()) {
                            Vector vector = new Vector((float) point2d.getX(), (float) point2d.getY(), 1);
                            vector = vector.cross(ctm);
                            yCoordsList.add(vector.get(Vector.I2));
                        }
                        if (!yCoordsList.isEmpty()) {
                            Float[] yCoords = yCoordsList.toArray(new Float[0]);
                            Arrays.sort(yCoords);
                            addVerticalUseSection(yCoords[0], yCoords[yCoords.length - 1]);
                        }
                    }
                }
                break;
            }
            case RENDER_TEXT: {
                TextRenderInfo renderInfo = (TextRenderInfo) data;
                LineSegment ascentLine = renderInfo.getAscentLine();
                LineSegment descentLine = renderInfo.getDescentLine();
                float[] yCoords = new float[]{
                        ascentLine.getStartPoint().get(Vector.I2),
                        ascentLine.getEndPoint().get(Vector.I2),
                        descentLine.getStartPoint().get(Vector.I2),
                        descentLine.getEndPoint().get(Vector.I2)
                };
                Arrays.sort(yCoords);
                addVerticalUseSection(yCoords[0], yCoords[3]);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public Set<EventType> getSupportedEvents() {
        return supportedEvents;
    }

    //
    // helper methods
    //

    /**
     * This method marks the given interval as used.
     */
    void addVerticalUseSection(float from, float to) {
        if (to < from) {
            float temp = to;
            to = from;
            from = temp;
        }

        int i = 0, j = 0;
        for (; i < verticalFlips.size(); i++) {
            float flip = verticalFlips.get(i);
            if (flip < from)
                continue;

            for (j = i; j < verticalFlips.size(); j++) {
                flip = verticalFlips.get(j);
                if (flip < to)
                    continue;
                break;
            }
            break;
        }
        boolean fromOutsideInterval = i % 2 == 0;
        boolean toOutsideInterval = j % 2 == 0;

        while (j-- > i)
            verticalFlips.remove(j);
        if (toOutsideInterval)
            verticalFlips.add(i, to);
        if (fromOutsideInterval)
            verticalFlips.add(i, from);
    }

}
