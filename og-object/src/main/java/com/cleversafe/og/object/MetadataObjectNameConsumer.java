/*
 * Copyright (C) 2005-2015 Cleversafe, Inc. All rights reserved.
 *
 * Contact Information: Cleversafe, Inc. 222 South Riverside Plaza Suite 1700 Chicago, IL 60606, USA
 *
 * licensing@cleversafe.com
 */

package com.cleversafe.og.object;

import java.util.Set;

import com.cleversafe.og.api.Operation;

/**
 * A {@code ObjectNameConsumer} implementation which consumes object names for metadata operations
 *
 * @since 1.0
 */
public class MetadataObjectNameConsumer extends AbstractObjectNameConsumer {
  /**
   * Constructs an instance
   *
   * @param objectManager the object manager for this instance to work with
   * @param statusCodes the status codes this instance should work with
   * @throws IllegalArgumentException if any status code in status codes is invalid
   */
  public MetadataObjectNameConsumer(final ObjectManager objectManager, final Set<Integer> statusCodes) {
    super(objectManager, Operation.METADATA, statusCodes);
  }

  @Override
  protected void updateObjectManager(final ObjectMetadata objectName) {
    this.objectManager.getComplete(objectName);
  }

  @Override
  public String toString() {
    return "OverwriteObjectNameConsumer []";
  }
}