/*
 * Copyright (C) 2005-2015 Cleversafe, Inc. All rights reserved.
 * 
 * Contact Information: Cleversafe, Inc. 222 South Riverside Plaza Suite 1700 Chicago, IL 60606, USA
 * 
 * licensing@cleversafe.com
 */

package com.cleversafe.og.soh;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.cleversafe.og.http.ResponseBodyConsumer;
import com.cleversafe.og.util.Context;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;

/**
 * A response body consumer which processes and returns an SOH object name from the response body of
 * an SOH PUT response
 * 
 * @since 1.0
 */
public class SOHWriteResponseBodyConsumer implements ResponseBodyConsumer {
  @Override
  public Map<String, String> consume(final int statusCode, final InputStream response)
      throws IOException {
    if (statusCode != 201) {
      return ImmutableMap.of();
    }
    checkNotNull(response);

    final BufferedReader reader =
        new BufferedReader(new InputStreamReader(response, Charsets.UTF_8));

    final String objectName = reader.readLine();
    while ((reader.readLine()) != null) {
      // consume the stream, for SOH writes there should never be anything else
    }
    return ImmutableMap.of(Context.X_OG_OBJECT_NAME, objectName);
  }

  @Override
  public String toString() {
    return "SOHWriteResponseBodyConsumer []";
  }
}
