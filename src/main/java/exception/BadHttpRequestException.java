package exception;

// NOTE: Checked Exception으로 반드시 프로그래머가 처리하도록 명시하는게 좋겟다.
// 왜냐면 client에게 에러 응답코드를 전송해야 하는 경우이기 때문.
public class BadHttpRequestException extends Exception {
  public BadHttpRequestException(String errMessage) {
    super(errMessage);
  }
}