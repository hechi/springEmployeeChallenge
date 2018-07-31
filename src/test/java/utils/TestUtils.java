package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.hechenberger.employee.model.Employee;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.Date;

/**
 * Helper Functions which are used in the tests
 */
public class TestUtils {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static <T> T readObj(Class clazz, ResultActions res) throws Exception {
    return mapper.readerFor(clazz).readValue(res.andReturn().getResponse().getContentAsString());
  }

  /**
   * converts an object to an json string
   *
   * @param o
   * @return corresponding json string of object
   */
  public static String json(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * create a dummy with a random name and email
   * @return employee object
   */
  public static Employee getRandomDummy() {
    return new Employee("jane.doe" + Math.random() + "@example.com", "Jane" + Math.random(), "Doe", new Date(), new String[]{"soccer", "music", "dance"});
  }
}
