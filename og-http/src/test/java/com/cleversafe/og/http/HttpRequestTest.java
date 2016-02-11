/*
 * Copyright (C) 2005-2015 Cleversafe, Inc. All rights reserved.
 * 
 * Contact Information: Cleversafe, Inc. 222 South Riverside Plaza Suite 1700 Chicago, IL 60606, USA
 * 
 * licensing@cleversafe.com
 */

package com.cleversafe.og.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cleversafe.og.api.Body;
import com.cleversafe.og.api.DataType;
import com.cleversafe.og.api.Method;

public class HttpRequestTest {
  private Method method;
  private URI uri;

  @Before
  public void before() throws URISyntaxException {
    this.method = Method.PUT;
    this.uri = new URI("/container/object");
  }

  @Test(expected = NullPointerException.class)
  public void nullMethod() {
    new HttpRequest.Builder(null, this.uri).build();
  }

  @Test(expected = NullPointerException.class)
  public void nullUri() {
    new HttpRequest.Builder(this.method, null).build();
  }

  @Test(expected = NullPointerException.class)
  public void nullKey() {
    new HttpRequest.Builder(this.method, this.uri).withHeader(null, "value").build();
  }

  @Test(expected = NullPointerException.class)
  public void nullValue() {
    new HttpRequest.Builder(this.method, this.uri).withHeader("key", null).build();
  }

  @Test(expected = NullPointerException.class)
  public void nullBody() {
    new HttpRequest.Builder(this.method, this.uri).withBody(null).build();
  }

  @Test
  public void method() {
    final Method method = new HttpRequest.Builder(Method.HEAD, this.uri).build().getMethod();
    assertThat(method, is(Method.HEAD));
  }

  @Test
  public void uri() {
    final URI uri = new HttpRequest.Builder(this.method, this.uri).build().getUri();
    assertThat(uri, is(this.uri));
  }

  @Test
  public void noQueryParameters() {
    final HttpRequest request = new HttpRequest.Builder(this.method, this.uri).build();
    assertThat(request.getQueryParameters().size(), is(0));
  }

  @Test
  public void oneQueryParameter() {
    final HttpRequest request =
        new HttpRequest.Builder(this.method, this.uri).withQueryParameter("key", "value").build();
    assertThat(request.getQueryParameters().size(), is(1));
    final List<String> values = request.getQueryParameters().get("key");
    assertThat(values.size(), is(1));
    assertThat(values.get(0), is("value"));
  }

  @Test(expected = NullPointerException.class)
  public void oneQueryParameterNullKey() {
    new HttpRequest.Builder(this.method, this.uri).withQueryParameter(null, "value");
  }

  @Test
  public void oneQueryParameterNullValue() {
    final HttpRequest request =
        new HttpRequest.Builder(this.method, this.uri).withQueryParameter("key", null).build();
    assertThat(request.getQueryParameters().size(), is(1));
    final List<String> values = request.getQueryParameters().get("key");
    assertThat(values.size(), is(1));
    assertThat(values.get(0), is((String) null));
  }

  @Test
  public void oneQueryParameterMultipleValues() {
    final HttpRequest request = new HttpRequest.Builder(this.method, this.uri)
        .withQueryParameter("key", "one").withQueryParameter("key", "two").build();
    assertThat(request.getQueryParameters().size(), is(1));
    final List<String> values = request.getQueryParameters().get("key");
    assertThat(values.size(), is(2));
    assertThat(values.get(0), is("one"));
    assertThat(values.get(1), is("two"));
  }

  @Test
  public void multipleQueryParameters() {
    final HttpRequest.Builder b = new HttpRequest.Builder(this.method, this.uri);
    for (int i = 0; i < 10; i++) {
      // (10 - i) exposes sorted vs insertion order
      b.withQueryParameter("key" + (10 - i), "value");
    }
    final HttpRequest request = b.build();
    assertThat(request.getQueryParameters().size(), is(10));

    for (int i = 0; i < 10; i++) {
      final List<String> values = request.getQueryParameters().get("key" + (10 - i));
      assertThat(values.size(), is(1));
      assertThat(values.get(0), is("value"));
    }
  }

  @Test
  public void queryParametersModification() {
    final HttpRequest.Builder b =
        new HttpRequest.Builder(this.method, this.uri).withQueryParameter("key1", "value1");
    final HttpRequest request = b.build();
    b.withQueryParameter("key2", "value2");
    assertThat(request.getQueryParameters().size(), is(1));
    final List<String> values = request.getQueryParameters().get("key1");
    assertThat(values.size(), is(1));
    assertThat(values.get(0), is("value1"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void queryParameterRemove() {
    new HttpRequest.Builder(this.method, this.uri).withQueryParameter("key", "value").build()
        .getQueryParameters().remove("key");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void queryParameterRemoveValue() {
    new HttpRequest.Builder(this.method, this.uri).withQueryParameter("key", "value").build()
        .getQueryParameters().get("key").remove(0);
  }

  @Test
  public void noHeaders() {
    final HttpRequest request = new HttpRequest.Builder(this.method, this.uri).build();
    // auto-generated Date header
    assertThat(request.headers().size(), is(1));
  }

  @Test
  public void oneHeader() {
    final HttpRequest request =
        new HttpRequest.Builder(this.method, this.uri).withHeader("key", "value").build();
    assertThat(request.headers().size(), is(2));
    assertThat(request.headers(), hasEntry("key", "value"));
  }

  @Test
  public void multipleHeaders() {
    final HttpRequest.Builder b = new HttpRequest.Builder(this.method, this.uri);
    for (int i = 0; i < 10; i++) {
      // (10 - i) exposes sorted vs insertion order
      b.withHeader("key" + (10 - i), "value");
    }
    final HttpRequest request = b.build();
    assertThat(request.headers().size(), is(11));

    for (int i = 0; i < 10; i++) {
      assertThat(request.headers(), hasEntry("key" + (10 - i), "value"));
    }
  }

  @Test
  public void headerModification() {
    final HttpRequest.Builder b =
        new HttpRequest.Builder(this.method, this.uri).withHeader("key1", "value1");
    final HttpRequest request = b.build();
    b.withHeader("key2", "value2");
    assertThat(request.headers(), hasEntry("key1", "value1"));
    assertThat(request.headers(), not(hasEntry("key2", "value2")));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void headerRemove() {
    new HttpRequest.Builder(this.method, this.uri).withHeader("key", "value").build().headers()
        .remove("key");
  }

  @Test
  public void defaultBody() {
    final Body body = new HttpRequest.Builder(this.method, this.uri).build().getBody();
    assertThat(body.getDataType(), is(DataType.NONE));
    assertThat(body.getSize(), is(0L));
  }

  @Test
  public void body() {
    final Body body = new HttpRequest.Builder(this.method, this.uri).withBody(Bodies.zeroes(12345))
        .build().getBody();
    assertThat(body.getDataType(), is(DataType.ZEROES));
    assertThat(body.getSize(), is(12345L));
  }
}
