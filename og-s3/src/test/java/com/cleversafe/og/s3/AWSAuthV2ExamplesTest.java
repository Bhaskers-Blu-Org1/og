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
// Date: Jun 18, 2014
// ---------------------

package com.cleversafe.og.s3;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.cleversafe.og.http.HttpRequest;
import com.cleversafe.og.operation.Metadata;
import com.cleversafe.og.operation.Method;
import com.cleversafe.og.operation.Request;
import com.cleversafe.og.s3.AWSAuthV2;

// test data pulled from examples aws auth signing v2 at:
// http://docs.aws.amazon.com/AmazonS3/latest/dev/RESTAuthentication.html
@RunWith(Parameterized.class)
public class AWSAuthV2ExamplesTest
{
   private static final String AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE";
   private static final String AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
   private final Request request;
   private final String stringToSign;
   private final String nextAuthorizationHeader;

   public AWSAuthV2ExamplesTest(
         final Request request,
         final String stringToSign,
         final String nextAuthorizationHeader)
   {
      this.request = request;
      this.stringToSign = stringToSign;
      this.nextAuthorizationHeader = nextAuthorizationHeader;
   }

   @Parameters
   public static Collection<Object[]> generateData() throws URISyntaxException
   {
      return Arrays.asList(new Object[][]{generateGET(), generatePUT(), generateList(),
            generateDELETE()});
   }

   public static Object[] generateGET() throws URISyntaxException
   {
      final Request request =
            new HttpRequest.Builder(Method.GET, new URI("/johnsmith/photos/puppy.jpg"))
                  .withHeader("Date", "Tue, 27 Mar 2007 19:36:42 +0000")
                  .withMetadata(Metadata.USERNAME, AWS_ACCESS_KEY_ID)
                  .withMetadata(Metadata.PASSWORD, AWS_SECRET_ACCESS_KEY)
                  .build();
      final String stringToSign =
            "GET\n\n\nTue, 27 Mar 2007 19:36:42 +0000\n/johnsmith/photos/puppy.jpg";
      final String nextAuthorizationHeader =
            "AWS AKIAIOSFODNN7EXAMPLE:bWq2s1WEIj+Ydj0vQ697zp+IXMU=";
      return new Object[]{request, stringToSign, nextAuthorizationHeader};
   }

   public static Object[] generatePUT() throws URISyntaxException
   {
      final Request request =
            new HttpRequest.Builder(Method.PUT, new URI("/johnsmith/photos/puppy.jpg"))
                  .withHeader("Content-Type", "image/jpeg")
                  .withHeader("Content-Length", "94328")
                  .withHeader("Date", "Tue, 27 Mar 2007 21:15:45 +0000")
                  .withMetadata(Metadata.USERNAME, AWS_ACCESS_KEY_ID)
                  .withMetadata(Metadata.PASSWORD, AWS_SECRET_ACCESS_KEY)
                  .build();
      final String stringToSign =
            "PUT\n\nimage/jpeg\nTue, 27 Mar 2007 21:15:45 +0000\n/johnsmith/photos/puppy.jpg";
      final String nextAuthorizationHeader =
            "AWS AKIAIOSFODNN7EXAMPLE:MyyxeRY7whkBe+bq8fHCL/2kKUg=";
      return new Object[]{request, stringToSign, nextAuthorizationHeader};
   }

   public static Object[] generateList() throws URISyntaxException
   {
      final Request request =
            new HttpRequest.Builder(Method.GET, new URI(
                  "/johnsmith/?prefix=photos&max-keys=50&marker=puppy"))
                  .withHeader("User-Agent", "Mozilla/5.0")
                  .withHeader("Date", "Tue, 27 Mar 2007 19:42:41 +0000")
                  .withMetadata(Metadata.USERNAME, AWS_ACCESS_KEY_ID)
                  .withMetadata(Metadata.PASSWORD, AWS_SECRET_ACCESS_KEY)
                  .build();
      final String stringToSign =
            "GET\n\n\nTue, 27 Mar 2007 19:42:41 +0000\n/johnsmith/";
      final String nextAuthorizationHeader =
            "AWS AKIAIOSFODNN7EXAMPLE:htDYFYduRNen8P9ZfE/s9SuKy0U=";
      return new Object[]{request, stringToSign, nextAuthorizationHeader};
   }

   public static Object[] generateDELETE() throws URISyntaxException
   {
      final Request request =
            new HttpRequest.Builder(Method.DELETE, new URI("/johnsmith/photos/puppy.jpg"))
                  .withHeader("User-Agent", "dotnet")
                  .withHeader("Host", "s3.amazonaws.com")
                  .withHeader("Date", "Tue, 27 Mar 2007 21:20:27 +0000")
                  .withHeader("x-amz-date", "Tue, 27 Mar 2007 21:20:26 +0000")
                  .withMetadata(Metadata.USERNAME, AWS_ACCESS_KEY_ID)
                  .withMetadata(Metadata.PASSWORD, AWS_SECRET_ACCESS_KEY)
                  .build();
      final String stringToSign =
            "DELETE\n\n\nTue, 27 Mar 2007 21:20:26 +0000\n/johnsmith/photos/puppy.jpg";
      final String nextAuthorizationHeader =
            "AWS AKIAIOSFODNN7EXAMPLE:lx3byBScXR6KzyMaifNkardMwNk=";
      return new Object[]{request, stringToSign, nextAuthorizationHeader};
   }

   @Test
   public void testSigning()
   {
      final AWSAuthV2 auth = new AWSAuthV2();
      final String s = auth.stringToSign(this.request);
      final String s2 = auth.nextAuthorizationHeader(this.request);
      Assert.assertEquals(this.stringToSign, s);
      Assert.assertEquals(this.nextAuthorizationHeader, s2);
   }
}