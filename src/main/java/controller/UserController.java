package controller;

import org.jetbrains.annotations.Nullable;
import java.lang.reflect.Method;

import minispring.RequestHandler;
import minispring.annotation.RequestMapping;
import minispring.annotation.RequestParam;
import minispring.DispatcherManagerProvider;
import minispring.http.base.HttpMethod;
import db.DataBase;
import model.User;

// @RestController
public class UserController {

  public UserController() throws NoSuchMethodException {
    Method createUser = UserController.class.getMethod("createUser", String.class, String.class, String.class, String.class);
    DispatcherManagerProvider.getManager().registerToRequestHandlerTable(
            createUser.getAnnotation(RequestMapping.class).method(),
            new RequestHandler(createUser));

    Method sayHelloWorld = UserController.class.getMethod("sayHelloWorld");
    DispatcherManagerProvider.getManager().registerToRequestHandlerTable(
            sayHelloWorld.getAnnotation(RequestMapping.class).method(),
            new RequestHandler(sayHelloWorld));
  }

  // GET /user/create?userId=kim&password=kk&name=js&email=miny@naver.com
  @RequestMapping(path = "/user/create", method = HttpMethod.GET)
  public String createUser(
          @RequestParam(name = "userId") @Nullable String userId,
          @RequestParam(name = "password") @Nullable String password,
          @RequestParam(name = "name") @Nullable String name,
          @RequestParam(name = "email") @Nullable String email)
  {
    User user = new User(userId, password, name, email);
    DataBase.addUser(user);
    return "<h1>회원 가입 성공!</h1>"; // TODO: handle null passed params (ex. [foo=&bar=baz])
  }

  @RequestMapping(path = "/helloworld", method = HttpMethod.GET)
  public String sayHelloWorld() {
    return "<h1>Hello, World! This is from UserController()!</h1>";
  }
}
