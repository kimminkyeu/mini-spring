package controller;

import com.google.common.net.HttpHeaders;
import minispring.ResponseModifier;
import minispring.annotation.RequestBody;
import minispring.exception.HttpClient4xxException;
import minispring.http.base.HttpContentType;
import minispring.http.base.HttpStatus;
import minispring.http.request.HttpRequest;
import minispring.http.response.HttpResponseBody;
import minispring.util.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import minispring.RequestHandler;
import minispring.annotation.RequestMapping;
import minispring.annotation.RequestParam;
import minispring.RequestHandlerStoreManager;
import minispring.http.base.HttpMethod;
import db.DataBase;
import model.User;

// @Controller
public class UserController {

  // TODO: on main init, find @Controller annotation + register methods with @RequestMapping
  public UserController() throws NoSuchMethodException {
    Method createUserGET = UserController.class.getMethod("createUser", String.class, String.class, String.class, String.class);
    RequestHandlerStoreManager.getInstance().register(
            createUserGET.getAnnotation(RequestMapping.class).method(),
            new RequestHandler(createUserGET));

    Method createUserPOST = UserController.class.getMethod("createUser", String.class);
    RequestHandlerStoreManager.getInstance().register(
            createUserPOST.getAnnotation(RequestMapping.class).method(),
            new RequestHandler(createUserPOST));

    Method loginUser = UserController.class.getMethod("loginUser", String.class);
    RequestHandlerStoreManager.getInstance().register(
            loginUser.getAnnotation(RequestMapping.class).method(),
            new RequestHandler(loginUser));

    Method getUsers = UserController.class.getMethod("getUsers", HttpRequest.class);
    RequestHandlerStoreManager.getInstance().register(
            getUsers.getAnnotation(RequestMapping.class).method(),
            new RequestHandler(getUsers));

    Method sayHelloWorld = UserController.class.getMethod("sayHelloWorld");
    RequestHandlerStoreManager.getInstance().register(
            sayHelloWorld.getAnnotation(RequestMapping.class).method(),
            new RequestHandler(sayHelloWorld));
  }

  @RequestMapping(path = "/helloworld", method = HttpMethod.GET)
  public String sayHelloWorld() {
    return "<h1>Hello, World! This is from UserController()!</h1>";
  }

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

  @RequestMapping(path = "/user/create", method = HttpMethod.POST)
  public ResponseModifier createUser(@RequestBody @Nullable String body) throws HttpClient4xxException {
    User user = convertQueryToUser(body);
    if (DataBase.findUserById(user.getUserId()) != null) {
      // TODO: 중복 회원 처리 ...
    }
    DataBase.addUser(user);

    return new ResponseModifier(response -> {
      response.getHeader().put(HttpHeaders.LOCATION, "/webapp/index.html");
      response.setHttpStatus(HttpStatus.FOUND); // redirect
    });
  }

  @RequestMapping(path = "/user/login", method = HttpMethod.POST)
  public ResponseModifier loginUser(@RequestBody @Nullable String body) throws HttpClient4xxException {
    User userToLogin = convertQueryToUser(body);
    User userInDB = DataBase.findUserById(userToLogin.getUserId());

    String loginStatusCookie = null;
    if ((userInDB != null) && userInDB.getPassword().equals(userToLogin.getPassword())) {
      loginStatusCookie = "logined=true";
    } else {
      loginStatusCookie = "logined=false";
    }

    final String finalLoginStatusCookie = loginStatusCookie;

    return new ResponseModifier(response -> {
      response.getHeader().put(HttpHeaders.SET_COOKIE, finalLoginStatusCookie);
      response.setHttpStatus(HttpStatus.FOUND); // redirect
    });
  }

  // NOTE: HttpRequest가 param에 들어있으면, 모든 Request를 그대로 집어 넣어준다.
  @RequestMapping(path = "/user/list", method = HttpMethod.GET)
  public ResponseModifier getUsers(@NotNull HttpRequest request) {
    Assert.notNull(request);

    Map<String, String> cookies = request.getHeader().getCookies();
    if ( (cookies != null) && (Boolean.parseBoolean(cookies.get("logined"))) ) {
      StringBuilder stringBuilder = new StringBuilder();
      for (User user : DataBase.findAll()) {
        stringBuilder.append("<h3>");
        stringBuilder.append("Name : ").append(user.getName()).append("  Email : ").append(user.getEmail());
        stringBuilder.append("</h3>");
      }
      final String listFinal = stringBuilder.toString();
      return new ResponseModifier(response -> {
        response.getHeader().setContentType(HttpContentType.TEXT_HTML);
        response.setBody(new HttpResponseBody(listFinal));
      });
    } else {
      return new ResponseModifier(response -> {
        response.getHeader().put(HttpHeaders.LOCATION, "/webapp/user/login.html");
        response.setHttpStatus(HttpStatus.FOUND); // redirect
      });
    }
  }


  @NotNull
  private User convertQueryToUser(@Nullable String query) throws HttpClient4xxException {
    if (query == null) {
      throw new HttpClient4xxException(HttpStatus.BAD_REQUEST, "query가 null입니다.");
    }

    final Map<String, String> queryPair = new HashMap<>();
    final String[] pairs = query.split("&");

    for (String pair : pairs) {
      int idx = pair.indexOf("=");
      if ( (idx < 0) || (idx + 1 == pair.length()) ) {
        throw new HttpClient4xxException(HttpStatus.BAD_REQUEST, "query 형식이 foo=bar 형식이 아닙니다. value는 반드시 존재해야 합니다!");
      }
      String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
      String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8); // email에서 @문자 때문.
      queryPair.put(key, value);
    }

    return new User(queryPair.get("userId"), queryPair.get("password"), queryPair.get("name"), queryPair.get("email"));
  }
}
