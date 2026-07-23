// Disabled: this was an early copy-paste draft, superseded by the SelectSlideCommand
// design built up interactively (Runnable, cancellable Task, calls a slide-server loader).
// Kept commented out rather than deleted while the real version is being built.
//
// package org.computational_immunology.ext.ImmuNet.ui;
//
// import java.util.List;
//
// import org.computational_immunology.ext.ImmuNet.core.ImmuNetLog;
// import org.computational_immunology.ext.ImmuNet.core.ServerConnectionHandler;
// import org.computational_immunology.ext.ImmuNet.core.ServerRequestHandler;
// import org.computational_immunology.ext.ImmuNet.core.StreamedImageServer;
// import org.computational_immunology.ext.ImmuNet.core.Tile;
//
// import java.io.IOException;
//
// import qupath.lib.gui.QuPathGUI;
// import qupath.lib.images.ImageData;
// import qupath.lib.images.servers.SparseImageServer;
// import qupath.lib.regions.ImageRegion;
//
//
// public class SelectSlideCommand implements Runnable {
//
// 	private ObservableValue<? extends QuPathViewer> viewerValue;
// 	private int zoomAmount;
//
//     public static void connectToServer(String username, String hostname, String password, String dbuser, String dbpass) throws Exception {
//         ServerConnectionHandler.getInstance().startSSHThread(username, hostname, password);
//         ServerConnectionHandler.getInstance().performDatabaseLogin(dbuser,dbpass);
//     }
//     public static void setStreamedServer(String datasetName, String slideName) {
//         try {
//             List<Tile> tiles = ServerRequestHandler.getAllTiles(
//                     datasetName, slideName
//             );
//             try {
//                 SparseImageServer server = createSparseImageServer(tiles);
//                 QuPathGUI.getInstance().getViewer().setImageData(new ImageData<>(server));
//                 ImmuNetLog.log("Successfully set Image Data.");
//             } catch (IOException e) {
//                 ImmuNetLog.error("Could not set image data",e);
//             }
//         } catch (IOException | InterruptedException e) {
//             ImmuNetLog.error("Could not fetch tiles. Are you connected to the server?",e);
//         }
//     }
//
//     private static SparseImageServer createSparseImageServer(List<Tile> tiles) throws IOException, InterruptedException {
//         SparseImageServer.Builder builder = new SparseImageServer.Builder();
//
//         for (var tile : tiles) {
//             StreamedImageServer tileServer = new StreamedImageServer(tile);
//             ImageRegion region = ImageRegion.createInstance(
//                     (int)tile.getTileX(),
//                     (int)tile.getTileY(),
//                     (int)tile.tileW, (int)tile.tileH, 1,0
//             );
//             ImmuNetLog.log("serverRegion add: ({}), ({}), ({}, {})", tileServer, region, tile.tileW, tile.tileH);
//             builder.serverRegion(region, 1, tileServer);
//         }
//
//         return builder.build();
//     }
//
//     private SelectSlideCommand() {
//         // This utility class should not be instantiated
//     }
//
// }
