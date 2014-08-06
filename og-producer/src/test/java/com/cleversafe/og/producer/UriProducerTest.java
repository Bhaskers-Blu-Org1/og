//
// Copyright (C) 2005-2011 Cleversafe, Inc. All rights reserved.
//
// Contact Information:
// Cleversafe, Inc.
// 222 South Riverside Plaza
// Suite 1700
// Chicago, IL 60606, USA
//
// licensing@cleversafe.com
//
// END-OF-HEADER
//
// -----------------------
// @author: rveitch
//
// Date: Jun 29, 2014
// ---------------------

package com.cleversafe.og.producer;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cleversafe.og.http.Scheme;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

public class UriProducerTest
{
   private String host;
   private String container;
   private List<Supplier<String>> path;

   @Before
   public void before()
   {
      this.host = "192.168.8.1";
      this.container = "container";
      this.path = Lists.newArrayList();
      this.path.add(Producers.of(this.container));
   }

   @Test(expected = NullPointerException.class)
   public void testNullScheme()
   {
      new UriProducer.Builder(this.host, this.path).withScheme((Scheme) null).build();
   }

   @Test(expected = NullPointerException.class)
   public void testNullSchemeSupplier()
   {
      new UriProducer.Builder(this.host, this.path).withScheme((Supplier<Scheme>) null).build();
   }

   @Test(expected = NullPointerException.class)
   public void testNullHost()
   {
      new UriProducer.Builder((String) null, this.path).build();
   }

   @Test(expected = NullPointerException.class)
   public void testNullHostSupplier()
   {
      new UriProducer.Builder((Supplier<String>) null, this.path).build();
   }

   @Test
   public void testNullPort()
   {
      // can set port to null, it gets ignored when assembling url in produce
      new UriProducer.Builder(this.host, this.path).onPort((Supplier<Integer>) null).build();
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNegativePort()
   {
      new UriProducer.Builder(this.host, this.path).onPort(-1).build();
   }

   @Test
   public void testZeroPort()
   {
      new UriProducer.Builder(this.host, this.path).onPort(0).build();
   }

   @Test
   public void testPositivePort()
   {
      new UriProducer.Builder(this.host, this.path).onPort(1).build();
   }

   @Test(expected = NullPointerException.class)
   public void testNullPath()
   {
      new UriProducer.Builder(this.host, (List<Supplier<String>>) null).build();
   }

   @Test(expected = NullPointerException.class)
   public void testQueryParametersNullKey()
   {
      new UriProducer.Builder(this.host, this.path).withQueryParameter(null, "value").build();
   }

   @Test(expected = NullPointerException.class)
   public void testQueryParametersNullValue()
   {
      new UriProducer.Builder(this.host, this.path).withQueryParameter("key", null).build();
   }

   @Test
   public void testUriSupplier()
   {
      final Supplier<URI> p = new UriProducer.Builder(this.host, this.path).build();
      final URI uri = p.get();
      Assert.assertEquals(Scheme.HTTP, Scheme.valueOf(uri.getScheme().toUpperCase(Locale.US)));
      Assert.assertEquals(this.host, uri.getHost());
      Assert.assertEquals(-1, uri.getPort());
      Assert.assertEquals("/" + this.container, uri.getPath());
      Assert.assertNull(uri.getQuery());
   }

   @Test
   public void testScheme()
   {
      final Supplier<URI> p =
            new UriProducer.Builder(this.host, this.path).withScheme(Scheme.HTTP).build();
      final URI uri = p.get();
      Assert.assertEquals(Scheme.HTTP, Scheme.valueOf(uri.getScheme().toUpperCase(Locale.US)));
   }

   @Test
   public void testSchemeSupplier()
   {
      final Supplier<URI> p =
            new UriProducer.Builder(this.host, this.path).withScheme(Producers.of(Scheme.HTTPS)).build();
      final URI uri = p.get();
      Assert.assertEquals(Scheme.HTTPS, Scheme.valueOf(uri.getScheme().toUpperCase(Locale.US)));
   }

   @Test
   public void testHost()
   {
      final Supplier<URI> p = new UriProducer.Builder("10.1.1.1", this.path).build();
      final URI uri = p.get();
      Assert.assertEquals("10.1.1.1", uri.getHost());
   }

   @Test
   public void testPort()
   {
      final Supplier<URI> p = new UriProducer.Builder(this.host, this.path).onPort(80).build();
      final URI uri = p.get();
      Assert.assertEquals(80, uri.getPort());
   }

   @Test
   public void testPortSupplier()
   {
      final Supplier<URI> p =
            new UriProducer.Builder(this.host, this.path).onPort(Producers.of(8080)).build();
      final URI uri = p.get();
      Assert.assertEquals(8080, uri.getPort());
   }

   @Test
   public void testPath()
   {
      final List<Supplier<String>> aPath = Lists.newArrayList();
      aPath.add(Producers.of("container"));
      aPath.add(Producers.of("object"));
      final Supplier<URI> p = new UriProducer.Builder(this.host, aPath).build();
      final URI uri = p.get();
      Assert.assertEquals("/container/object", uri.getPath());
   }

   @Test
   public void testTrailingSlash()
   {
      final Supplier<URI> p =
            new UriProducer.Builder(this.host, this.path).withTrailingSlash().build();
      final URI uri = p.get();
      Assert.assertEquals("/container/", uri.getPath());
   }

   @Test
   public void testQueryParameters()
   {
      final Supplier<URI> p =
            new UriProducer.Builder(this.host, this.path).withQueryParameter("key", "value").build();
      final URI uri = p.get();
      Assert.assertEquals("key=value", uri.getQuery());
   }

   @Test
   public void testQueryParameters2()
   {
      final Supplier<URI> p =
            new UriProducer.Builder(this.host, this.path).withQueryParameter("key", "value").withQueryParameter(
                  "key2", "value2").build();
      final URI uri = p.get();
      Assert.assertEquals("key=value&key2=value2", uri.getQuery());
   }

   @Test
   public void testQueryParameters3()
   {
      final Supplier<URI> p =
            new UriProducer.Builder(this.host, this.path).withQueryParameter("key2", "value2").withQueryParameter(
                  "key1", "value1").build();
      final URI uri = p.get();
      Assert.assertEquals("key2=value2&key1=value1", uri.getQuery());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testBadUri()
   {
      final List<Supplier<String>> badPath = Lists.newArrayList();
      badPath.add(Producers.of("containe\r"));
      final Supplier<URI> p = new UriProducer.Builder(this.host, badPath).build();
      p.get();
   }

   @Test
   public void testPathModification()
   {
      final List<Supplier<String>> path = Lists.newArrayList();
      path.add(Producers.of("container"));
      final Supplier<URI> p = new UriProducer.Builder(this.host, this.path).build();
      path.add(Producers.of("object"));
      final URI u = p.get();
      Assert.assertEquals("/container", u.getPath());
   }

   @Test
   public void testQueryParametersModification()
   {
      final UriProducer.Builder b =
            new UriProducer.Builder(this.host, this.path).withQueryParameter("key", "value");
      final Supplier<URI> p = b.build();
      b.withQueryParameter("key2", "value2");
      final URI uri = p.get();
      Assert.assertEquals("key=value", uri.getQuery());
   }
}