/* Copyright (c) IBM Corporation 2016. All Rights Reserved.
 * Project name: Object Generator
 * This project is licensed under the Apache License 2.0, see LICENSE.
 */

package com.ibm.og.test.condition;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.og.api.Request;
import com.ibm.og.api.Response;
import com.ibm.og.http.HttpUtil;
import com.ibm.og.statistic.Statistics;
import com.ibm.og.test.LoadTest;
import com.ibm.og.api.Operation;
import com.ibm.og.util.Pair;
import com.google.common.eventbus.Subscribe;

/**
 * A test condition which is triggered when a status code counter reaches a threshold value
 * 
 * @since 1.0
 */
public class StatusCodeCondition implements TestCondition {
  private static final Logger _logger = LoggerFactory.getLogger(StatusCodeCondition.class);
  private final Operation operation;
  private final int statusCode;
  private final long thresholdValue;
  private final LoadTest test;
  private final Statistics stats;
  private final boolean failureCondition;

  /**
   * Creates an instance
   * 
   * @param operation the operation type to query
   * @param statusCode the status code to query
   * @param thresholdValue the value at which this condition should be triggered
   * @param test the load test to stop when this condition is triggered
   * @param stats the statistics instance to query
   * @throws NullPointerException if operation, test, or stats is null
   * @throws IllegalArgumentException if thresholdValue is zero or negative, or if statusCode is not
   *         a valid status code
   */
  public StatusCodeCondition(final Operation operation, final int statusCode,
      final long thresholdValue, final LoadTest test, final Statistics stats, final boolean failureCondition) {
    this.operation = checkNotNull(operation);
    checkArgument(HttpUtil.VALID_STATUS_CODES.contains(statusCode),
        "statusCode must be a valid status code [%s]", statusCode);
    this.statusCode = statusCode;
    checkArgument(thresholdValue > 0, "thresholdValue must be > 0 [%s]", thresholdValue);
    this.thresholdValue = thresholdValue;
    this.test = checkNotNull(test);
    this.stats = checkNotNull(stats);
    this.failureCondition= failureCondition;
  }

  /**
   * Triggers a check of this condition
   * 
   * @param operation a completed request
   */
  @Subscribe
  public void update(final Pair<Request, Response> operation) {
    if (isTriggered()) {
      if (this.failureCondition) {
        this.test.abortTest(String.format("Failed Condition: %s", toString()));
      } else {
        this.test.stopTest();
      }
    }
  }

  @Override
  public boolean isTriggered() {
    final long currentValue = this.stats.getStatusCode(this.operation, this.statusCode);
    if (currentValue >= this.thresholdValue) {
      _logger.info("{} is triggered [{}]", toString(), currentValue);
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("StatusCodeCondition [%n" + "operation=%s,%n" + "statusCode=%s,%n"
        + "thresholdValue=%s%n" + "]", this.operation, this.statusCode, this.thresholdValue);
  }
}
