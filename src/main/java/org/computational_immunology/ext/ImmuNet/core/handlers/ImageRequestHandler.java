package org.computational_immunology.ext.ImmuNet.core.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.computational_immunology.ext.ImmuNet.core.ImmuNetLog;
import org.computational_immunology.ext.ImmuNet.core.TileMetadata;
import org.computational_immunology.ext.ImmuNet.core.TileMetadata.ImageType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageRequestHandler {
    private final PageFetcher pageFetcher;

    public record Point(double x, double y) {}

    private Map<Point, String> slideMap;

    public ImageRequestHandler(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
    }

    // Get list of tiles belonging to a slide
    public List<TileMetadata> getAllTiles(String datasetName, String slideName) throws IOException, InterruptedException {
        String path = "v/datasets/" + datasetName + '/' + slideName + '/';
        List<TileMetadata> tileMetadatas;
        String allTilesJson = pageFetcher.fetchStringPage(path).body();

        ImmuNetLog.log("Path at getAllTiles is " + path);
        ImmuNetLog.log(allTilesJson);

        JSONArray parsedOutput = new JSONArray(allTilesJson);
        ImmuNetLog.log("Fetched all tiles json");
        tileMetadatas = jsonToTileMetadatas(parsedOutput, ImageType.THUMB);
        return tileMetadatas;
    }

    // Convert json to list of tiles
    private List<TileMetadata> jsonToTileMetadatas(JSONArray array, ImageType type) throws IOException, InterruptedException {
        Map<Point, String> coordsToCode = new HashMap<>();
        List<TileMetadata> tileMetadatas = new ArrayList<>();
        ImmuNetLog.log("converting json to tiles");
        for (int i = 0; i < array.length(); i++) {
            JSONObject tile = array.getJSONObject(i);
            tileMetadatas.add(jsonToTileMetadata(tile, type));
            Point tileCoord = new Point(tile.getDouble("x"), tile.getDouble("y"));
            coordsToCode.put(tileCoord, tile.getString("code")); // map coordinates of a tile to its code
        }
        slideMap = coordsToCode;
        ImmuNetLog.log("tiles converted");
        return tileMetadatas;
    }

    public List<TileMetadata> testJsonToTileMetadatas(JSONArray array, ImageType type) throws IOException, InterruptedException {
        return jsonToTileMetadatas(array, type);
    }

    private TileMetadata jsonToTileMetadata(JSONObject json, ImageType type) throws IOException, JSONException {
        return new TileMetadata(
                json.getInt("id"),
                json.getString("code"),
                type,
                json.getDouble("x"),
                json.getDouble("y"),
                json.getDouble("width"),
                json.getDouble("height")
        );
    }

    public TileMetadata testJsonToTileMetadata(JSONObject json, ImageType type) throws JSONException, IOException {
        return jsonToTileMetadata(json, type);
    }

    /**
     * Fetch a list of format JSON from v/{webpageString} and converts to List of type String.
     *
     * @param localPath path to webpage
     * @return list of type String obtained from JSON webpage OR empty list on exception
     */
    public List<String> getWebpageAsList(String localPath) {
        List<String> datasetList = new ArrayList<>();
        try {
            String json = pageFetcher.fetchStringPage("v/" + localPath).body();
            JSONArray parsedDatasetList = new JSONArray(json);

            // Add each element of the JSONArray to the list to convert types
            for (int i = 0; i < parsedDatasetList.length(); i++) {
                datasetList.add(parsedDatasetList.getString(i));
            }
        } catch (Exception e) {
            ImmuNetLog.error("Error in fetching webpage list of items. Localpath: " + localPath, e);
        }
        return datasetList;
    }

    public Map<Point, String> getSlideMap() {
        return slideMap;
    }
}
