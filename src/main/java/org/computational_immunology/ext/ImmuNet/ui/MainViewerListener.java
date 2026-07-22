package org.computational_immunology.ext.ImmuNet.ui;

import org.computational_immunology.ext.ImmuNet.core.ImmuNetLog;

import java.awt.Shape;
import java.awt.image.BufferedImage;

import qupath.lib.gui.viewer.QuPathViewer;
import qupath.lib.gui.viewer.QuPathViewerListener;
import qupath.lib.images.ImageData;
import qupath.lib.objects.PathObject;

/**
 * A listener on the main viewer, for which we show the slide. It listens for changes in the viewer,
 * and specifically, whether the visible region has changed in zoom level.
 * We keep a boolean with this information
 * related to a QuPathViewer has changed in some way - such as by changing 
 * the underlying ImageData, or by moving the field of view.
 * 
 * 
 *
 */
public class MainViewerListener implements QuPathViewerListener {

    private static final double TARGET_DOWNSAMPLE = 1.0 / 25.0;
    private static final double EPSILON = 1e-4;

    @Override
    public void visibleRegionChanged(QuPathViewer viewer, Shape shape) {
        double downsample = viewer.getDownsampleFactor();
        if (downsample < TARGET_DOWNSAMPLE) {
            ImmuNetLog.log("downsample is less than target downsample");
        }
    }

    @Override
    public void imageDataChanged(QuPathViewer viewer, ImageData<BufferedImage> imageDataOld, ImageData<BufferedImage> imageDataNew) {}

    @Override
    public void selectedObjectChanged(QuPathViewer viewer, PathObject pathObjectSelected) {}

    @Override
    public void viewerClosed(QuPathViewer viewer) {}
}