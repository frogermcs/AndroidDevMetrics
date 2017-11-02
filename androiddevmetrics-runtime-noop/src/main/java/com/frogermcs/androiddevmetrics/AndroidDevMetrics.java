package com.frogermcs.androiddevmetrics;

import android.content.Context;

/**
 * Created by williamwebb on 3/2/16.
 */
// TODO: generate at compiletime to match really impl signature
public class AndroidDevMetrics {

  private int dagger2WarningLevel1, dagger2WarningLevel2, dagger2WarningLevel3;

  static final AndroidDevMetrics singleton = new AndroidDevMetrics(null);

  /** stub **/
  public static AndroidDevMetrics initWith(Context context) {
    return singleton;
  }

  /** stub **/
  public static AndroidDevMetrics initWith(Builder builder) {
    return singleton;
  }

  /** stub **/
  public static AndroidDevMetrics initWith(AndroidDevMetrics androidDevMetrics) {
    return singleton;
  }

  /** stub **/
  private static void setAndroidDevMetrics(AndroidDevMetrics androidDevMetrics) { }

  /** stub **/
  public static AndroidDevMetrics singleton() {
    return singleton;
  }

  AndroidDevMetrics(Context context) { }

  /** stub **/
  public int dagger2WarningLevel1() {
    return dagger2WarningLevel1;
  }

  /** stub **/
  public int dagger2WarningLevel2() {
    return dagger2WarningLevel2;
  }

  /** stub **/
  public int dagger2WarningLevel3() {
    return dagger2WarningLevel3;
  }

  /** stub **/
  private void setupMetrics() { }

  /** stub **/
  private void showNotification() { }

  /** stub **/
  public static class Builder {

    /** stub **/
    public Builder(Context context) { }

    public Builder dagger2WarningLevelsMs(int warning1, int warning2, int warning3) {
      return this;
    }

    /** stub **/
    public Builder frameDropsLimits(int measureIntervalMillis, double maxFpsForFrameDrop) {
      return this;
    }

    /** stub **/
    public Builder enableActivityMetrics(boolean enable) {
      return this;
    }

    /** stub **/
    public Builder showNotification(boolean show) {
      return this;
    }

    /** stub **/
    public Builder enableDagger2Metrics(boolean enable) {
      return this;
    }
    
    /** stub **/
    public Builder autoCancelNotification(boolean autoCancelNotification) {
      return this;
    }

    public Builder addUIInterceptor(Object interceptor) {
      return this;
    }

    /** stub **/
    public AndroidDevMetrics build() {
      return new AndroidDevMetrics(null);
    }
  }

}

