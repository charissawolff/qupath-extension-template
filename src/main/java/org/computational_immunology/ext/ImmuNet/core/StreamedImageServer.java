package org.computational_immunology.ext.ImmuNet.core;

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
    public StreamedImageServer(Tile Tile) {
        super(BufferedImage.class);
        this.OwnedTile = Tile;
    }

    private Tile OwnedTile;

    @Override
    public synchronized ImageServerMetadata getMetadata() {
        try {
            final int width = (int)OwnedTile.getTileW();
            final int height = (int)OwnedTile.getTileH();

            return new ImageServerMetadata.Builder()
                    .width(width)
                    .height(height)
                    // Temporary: code stands in for a real identifier until dataset/slide
                    // context (SlideRef) is threaded through to StreamedImageServer.
                    .name(OwnedTile.getCode())
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
        return OwnedTile.getCode();
    }

    @Override
    public Collection<URI> getURIs() {
        return List.of(URI.create(OwnedTile.getCode()));
    }

    @Override
    public BufferedImage readRegion(RegionRequest request) throws IOException {
        ImmuNetLog.log("readRegion: {}, {}", request, getPath());
        return OwnedTile.getImage().getSubimage(request.getX(), request.getY(), request.getWidth(),
                request.getHeight());
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
