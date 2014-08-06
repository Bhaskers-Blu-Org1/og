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
// Date: Mar 29, 2014
// ---------------------

package com.cleversafe.og.producer;

import static com.google.common.base.Preconditions.checkNotNull;

import com.cleversafe.og.object.ObjectManager;
import com.google.common.base.Supplier;

public class ReadObjectNameProducer implements Supplier<String>
{
   private final ObjectManager objectManager;

   public ReadObjectNameProducer(final ObjectManager objectManager)
   {
      this.objectManager = checkNotNull(objectManager);
   }

   @Override
   public String get()
   {
      return this.objectManager.acquireNameForRead().toString();
   }

   @Override
   public String toString()
   {
      return "ReadObjectNameProducer []";
   }
}