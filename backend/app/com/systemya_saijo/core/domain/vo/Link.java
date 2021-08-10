package com.systemya_saijo.core.domain.vo;

import play.mvc.Http;

public class Link extends StringValueObject{
  public Link(String value) {
    super(value);
  }

  public static Link of(Http.Request request, Id id) {
    var protocol = request.secure() ? "https://" : "http://";
    var host = request.host();
    var path = request.path();
    return new Link(protocol + host + path + "/" + id.getValue());
  }
}
