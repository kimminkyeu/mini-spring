package controller;

import minispring.RequestHandler;
import minispring.annotation.PathVariable;
import minispring.annotation.RequestMapping;
import minispring.DispatcherManagerProvider;
import minispring.http.base.HttpMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

// @Controller
public class FileController {

  public FileController() throws NoSuchMethodException {
    Method getStaticFile = FileController.class.getMethod("getStaticFile", String.class);
      DispatcherManagerProvider.getManager().registerToRequestHandlerTable(
              getStaticFile.getAnnotation(RequestMapping.class).method(),
              new RequestHandler(getStaticFile));
  }

  @RequestMapping(path = "/webapp/{filePath}", method = HttpMethod.GET)
  public String getStaticFile(@PathVariable String filePath) throws IOException {
    Path path = new File("./webapp" + "/" + filePath).toPath();
    String mimeType = Files.probeContentType(path);
    return new String(Files.readAllBytes(path));
  }
}
