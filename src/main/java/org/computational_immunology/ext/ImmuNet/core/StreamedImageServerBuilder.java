package org.computational_immunology.ext.ImmuNet.core;

import qupath.lib.images.servers.ImageServer;
import qupath.lib.images.servers.ImageServerBuilder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;

public class StreamedImageServerBuilder implements ImageServerBuilder<BufferedImage> {
    Tile ownedTile;

    @Override
    public UriImageSupport<BufferedImage> checkImageSupport(URI uri, String... args) throws IOException {
        return null;
    }

    @Override
    public ImageServer<BufferedImage> buildServer(URI uri, String... args) throws Exception {
        return new StreamedImageServer(ownedTile);
    }

    public StreamedImageServerBuilder tile(Tile inTile){
        ownedTile = inTile;
        return this;
    }

    @Override
    public String getName() {
        return "Streamed Image Server Builder";
    }

    @Override
    public String getDescription() {
        return "Builder for streamed png.";
    }

    @Override
    public Class<BufferedImage> getImageType() {
        return BufferedImage.class;
    }
}
