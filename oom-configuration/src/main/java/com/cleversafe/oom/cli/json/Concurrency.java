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
// Date: Mar 24, 2014
// ---------------------

package com.cleversafe.oom.cli.json;

import java.util.concurrent.TimeUnit;

public class Concurrency
{
   String type;
   long count;
   TimeUnit unit;
   long rampup;
   TimeUnit rampupUnit;

   public Concurrency()
   {
      this.type = "threads";
      this.count = 1;
      this.unit = TimeUnit.SECONDS;
      this.rampupUnit = TimeUnit.SECONDS;
   }

   /**
    * @return the type
    */
   public String getType()
   {
      return this.type;
   }

   /**
    * @return the count
    */
   public long getCount()
   {
      return this.count;
   }

   /**
    * @return the unit
    */
   public TimeUnit getUnit()
   {
      return this.unit;
   }

   /**
    * @return the rampup
    */
   public long getRampup()
   {
      return this.rampup;
   }

   /**
    * @return the rampupUnit
    */
   public TimeUnit getRampupUnit()
   {
      return this.rampupUnit;
   }

}