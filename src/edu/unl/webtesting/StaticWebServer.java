package edu.unl.webtesting;

import java.io.File;
import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;

import com.google.common.collect.Maps;

/**
 * Created by gigony on 12/12/14.
 */
public class StaticWebServer {
  private Vertx vertx;
  private HttpServer server;
  private String startPath = "";

  private static Map<Integer, HttpServer> portMap = Maps.newHashMap();

  public StaticWebServer(final String path) {
    this.startPath = new File(path).getAbsolutePath();
    this.vertx = VertxFactory.newVertx();
    this.server = this.vertx.createHttpServer();
  }

  public final void start(final int port) {

    final String rootPath = this.startPath;

    this.server.requestHandler(new Handler<HttpServerRequest>() {
      public void handle(final HttpServerRequest req) {
        String file = "";
        if (req.path().equals("/")) {
          file = rootPath + "index.html";
        } else if (!req.path().contains("..")) {
          file = rootPath + req.path();
        }
        req.response().sendFile(file);
      }
    }).listen(port, "localhost");
  }

  public final Vertx getVertx() {
    return this.vertx;
  }

  public final HttpServer getServer() {
    return this.server;
  }

  public static void start(final String path) {
    StaticWebServer.start(path, 8080);
  }

  public static void start(final String path, final int port) {
    if (portMap.containsKey(port)) {
      HttpServer httpServer = portMap.get(port);
      httpServer.close(); // close server if duplicate
      portMap.remove(port);
    }

    StaticWebServer server = new StaticWebServer(path);
    server.start(port);

    portMap.put(port, server.getServer());
  }

}
