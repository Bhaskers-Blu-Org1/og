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
// Date: Mar 12, 2014
// ---------------------

package com.cleversafe.og.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.cleversafe.og.api.Body;
import com.cleversafe.og.api.Data;

/**
 * A utility class for creating body instances
 * 
 * @since 1.0
 */
public class Bodies
{
   private static final Body NONE_BODY = Bodies.create(Data.NONE, 0);

   private Bodies()
   {}

   /**
    * Creates a body instance representing no body
    * 
    * @return an body instance
    */
   public static Body none()
   {
      return NONE_BODY;
   }

   /**
    * Creates a body instance representing a body with random data
    * 
    * @param size
    *           the size of the body
    * @return a random body instance
    * @throws IllegalArgumentException
    *            if size is negative
    */
   public static Body random(final long size)
   {
      return create(Data.RANDOM, size);
   }

   /**
    * Creates a body instance representing a body with zeroes for data
    * 
    * @param size
    *           the size of the body
    * @return a zero based body instance
    * @throws IllegalArgumentException
    *            if size is negative
    */
   public static Body zeroes(final long size)
   {
      return create(Data.ZEROES, size);
   }

   private static Body create(final Data data, final long size)
   {
      checkNotNull(data);
      checkArgument(size >= 0, "size must be >= 0 [%s]", size);

      return new Body()
      {
         @Override
         public Data getData()
         {
            return data;
         }

         @Override
         public long getSize()
         {
            return size;
         }
      };
   }
}
