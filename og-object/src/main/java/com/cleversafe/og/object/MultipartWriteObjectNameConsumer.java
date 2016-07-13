/*
 * Copyright (C) 2005-2015 Cleversafe, Inc. All rights reserved.
 * 
 * Contact Information: Cleversafe, Inc. 222 South Riverside Plaza Suite 1700 Chicago, IL 60606, USA
 * 
 * licensing@cleversafe.com
 */

package com.cleversafe.og.object;

import com.cleversafe.og.api.Operation;

import java.util.Set;

/**
 * A {@code ObjectNameConsumer} implementation which consumes object names for multipart write operations
 * 
 * @since 1.0
 */
public class MultipartWriteObjectNameConsumer extends AbstractObjectNameConsumer {
  /**
   * Constructs an instance
   *
   * @param objectManager the object manager for this instance to work with
   * @param statusCodes the status codes this instance should work with
   * @throws IllegalArgumentException if any status code in status codes is invalid
   */
  public MultipartWriteObjectNameConsumer(final ObjectManager objectManager,
      final Set<Integer> statusCodes) {
    super(objectManager, Operation.MULTIPART_WRITE_COMPLETE, statusCodes);
  }

  @Override
  protected void updateObjectManager(final ObjectMetadata objectName) {
    this.objectManager.add(objectName);
  }

  @Override
  public String toString() {
    return "MultipartWriteObjectNameConsumer []";
  }
}