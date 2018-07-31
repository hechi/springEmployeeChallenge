package utils;

import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.Charset;

public class HttpRequestCaller {

  private final MockMvc mockMvc;
  private final String url;
  private static MediaType CONTENTTYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));

  public HttpRequestCaller(MockMvc mockMvc, String url){
    this.mockMvc = mockMvc;
    this.url = url;
  }

  public ResultActions get() throws Exception{
    return get(null,null);
  }

  public ResultActions get(SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user) throws Exception{
    return get(null,user);
  }

  public ResultActions get(String param) throws Exception{
    return get(param,null);
  }

  public ResultActions get(String param, SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user) throws Exception{
    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url+(param!=null?param:"")).contentType(CONTENTTYPE);
    if(user!=null){
      request.with(user);
    }
    return mockMvc.perform(request);
  }

  public ResultActions post(Object body) throws Exception{
    return post(body,null);
  }

  public ResultActions post(Object body, SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user) throws Exception{
    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(url).contentType(CONTENTTYPE).content(TestUtils.json(body));
    if(user!=null){
      request.with(user);
    }
    return mockMvc.perform(request);
  }

  public ResultActions put(Object body) throws Exception{
    return put(body,null);
  }

  public ResultActions put(Object body, SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user) throws Exception{
    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(url).contentType(CONTENTTYPE).content(TestUtils.json(body));
    if(user!=null){
      request.with(user);
    }
    return mockMvc.perform(request);
  }

  public ResultActions delete(Object body) throws Exception{
    return delete(body,null);
  }

  public ResultActions delete(Object body, SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user) throws Exception{
    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(url).contentType(CONTENTTYPE).content(TestUtils.json(body));
    if(user!=null){
      request.with(user);
    }
    return mockMvc.perform(request);
  }
}
