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
 * I think a good approach is to be that when the zoom is under the threshold, to not keep updating (listening)
 * on the viewer, only when it was zoomed out and then crosses the boundary to become zoomed in.
 * Then we fire something, in this case an api call to the server to fetch higher quality tiles.
 */
public class MainViewerListener implements QuPathViewerListener {

    // make it 4 times smaller than what was originally set.
    private static final double TARGET_DOWNSAMPLE = 1.0 / 4.0;
    private static boolean isZoomedIn = false;
    private boolean wasZoomedIn = false; 

    @Override
    public void visibleRegionChanged(QuPathViewer viewer, Shape shape) {
        double downsample = viewer.getDownsampleFactor();
        boolean isZoomedInNow = downsample < TARGET_DOWNSAMPLE;
        if (isZoomedInNow && !wasZoomedIn) {
                ImmuNetLog.log("crossed into zoomed-in range");
                // this is the one moment to fire your callback
            }
        wasZoomedIn = isZoomedInNow;
    }

    @Override
    public void imageDataChanged(QuPathViewer viewer, ImageData<BufferedImage> imageDataOld, ImageData<BufferedImage> imageDataNew) {}

    @Override
    public void selectedObjectChanged(QuPathViewer viewer, PathObject pathObjectSelected) {}

    @Override
    public void viewerClosed(QuPathViewer viewer) {}
}