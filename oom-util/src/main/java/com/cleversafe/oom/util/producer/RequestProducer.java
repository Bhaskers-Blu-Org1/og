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
// Date: Mar 19, 2014
// ---------------------

package com.cleversafe.oom.util.producer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;
import java.util.Map;

import com.cleversafe.oom.api.Producer;
import com.cleversafe.oom.operation.Entity;
import com.cleversafe.oom.operation.Method;
import com.cleversafe.oom.operation.Request;
import com.cleversafe.oom.operation.RequestContext;

public class RequestProducer implements Producer<Request>
{
   private final Producer<Long> id;
   private final Producer<String> customRequestKey;
   private final Producer<Method> method;
   private final Producer<URL> url;
   private final Producer<Map<String, String>> headers;
   private final Producer<Entity> entity;
   private final Producer<Map<String, String>> metadata;

   public RequestProducer(
         final Producer<Long> id,
         final Producer<String> customRequestKey,
         final Producer<Method> method,
         final Producer<URL> url,
         final Producer<Map<String, String>> headers,
         final Producer<Entity> entity,
         final Producer<Map<String, String>> metadata)
   {
      this.id = checkNotNull(id, "id must not be null");
      this.customRequestKey = checkNotNull(customRequestKey, "customRequestKey must not be null");
      this.method = checkNotNull(method, "method must not be null");
      this.url = checkNotNull(url, "url must not be null");
      this.headers = checkNotNull(headers, "headers must not be null");
      this.entity = checkNotNull(entity, "entity must not be null");
      this.metadata = checkNotNull(metadata, "metadata must not be null");
   }

   @Override
   public Request produce(final RequestContext context)
   {
      return context.withId(this.id.produce(context))
            .withCustomRequestKey(this.customRequestKey.produce(context))
            .withMethod(this.method.produce(context))
            .withURL(this.url.produce(context))
            .withHeaders(this.headers.produce(context))
            .withEntity(this.entity.produce(context))
            .withMetaData(this.metadata.produce(context))
            .build();
   }
}