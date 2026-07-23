package org.computational_immunology.ext.ImmuNet.core;

import org.computational_immunology.ext.ImmuNet.core.TileMetadata;
import org.computational_immunology.ext.ImmuNet.core.handlers.ImageRequestHandler;

import qupath.lib.images.servers.ImageServerMetadata;
import qupath.lib.images.servers.ImageServerBuilder;
import qupath.lib.images.servers.AbstractImageServer;
import qupath.lib.images.servers.ImageChannel;
import qupath.lib.images.servers.PixelType;
import qupath.lib.regions.RegionRequest;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;


public class StreamedImageServer extends AbstractImageServer<BufferedImage> {
    private TileMetadata tileMetadata;
    private String datasetName;
    private String slideName;
    private ImageRequestHandler imageRequestHandler;
    

    public StreamedImageServer(TileMetadata tileMetadata, String datasetName, String slideName, ImageRequestHandler imageRequestHandler) {
        super(BufferedImage.class);
        this.tileMetadata = tileMetadata;
        this.datasetName = datasetName;
        this.slideName = slideName;
        this.imageRequestHandler = imageRequestHandler;
    }

    @Override
    public synchronized ImageServerMetadata getMetadata() {
        try {
            final int width = (int)tileMetadata.getWidth();
            final int height = (int)tileMetadata.getHeight();

            return new ImageServerMetadata.Builder()
                    .width(width)
                    .height(height)
                    // Temporary: code stands in for a real identifier until dataset/slide
                    // context (SlideRef) is threaded through to StreamedImageServer.
                    .name(tileMetadata.getCode())
                    .channels(ImageChannel.getDefaultRGBChannels())
                    .sizeZ(0)
                    .sizeT(0)
                    .rgb(true)
                    .pixelType(PixelType.UINT8)
                    .preferredTileSize(width,height).build();
        } catch (Exception e) {
            ImmuNetLog.error("Couldn't get metadata of ImageServer", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected ImageServerBuilder.ServerBuilder<BufferedImage> createServerBuilder() {
        return ImageServerBuilder.DefaultImageServerBuilder.createInstance(StreamedImageServerBuilder.class,
                URI.create(createID()), "");
    }

    @Override
    protected String createID() {
        return tileMetadata.getCode();
    }

    @Override
    public Collection<URI> getURIs() {
        return List.of(URI.create(tileMetadata.getCode()));
    }

    @Override
    public BufferedImage readRegion(RegionRequest request) throws IOException {
        ImmuNetLog.log("readRegion: {}", request);
        try {
            Tile fetchedTile = imageRequestHandler.fetchTileImage(tileMetadata, datasetName, slideName);
            return fetchedTile.getImage();
        } catch (IOException | InterruptedException e) {
            ImmuNetLog.error("Error fetching tile image", e);
            throw new IOException("Error fetching tile image that exists according to the database. Tile code: " + tileMetadata.getCode(), e);
        }
    }

    @Override
    public String getServerType() {
        return "StreamedImageServer";
    }

    @Override
    public ImageServerMetadata getOriginalMetadata() {
        return null;
    }
}
