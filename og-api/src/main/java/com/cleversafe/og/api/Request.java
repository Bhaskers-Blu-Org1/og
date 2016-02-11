/*
 * Copyright (C) 2005-2015 Cleversafe, Inc. All rights reserved.
 * 
 * Contact Information: Cleversafe, Inc. 222 South Riverside Plaza Suite 1700 Chicago, IL 60606, USA
 * 
 * licensing@cleversafe.com
 */

package com.cleversafe.og.api;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * An object that describes an http request
 * 
 * @since 1.0
 */
public interface Request extends Message {
  /**
   * Gets the http method for this request
   * 
   * @return the http method for this request
   * @see Method
   */
  Method getMethod();

  /**
   * Gets the uri for this request
   * 
   * @return the uri for this request
   */
  URI getUri();

  /**
   * Gets the query parameters for this request
   * 
   * @return the query parameters for this request
   */
  Map<String, List<String>> getQueryParameters();
}
