package org.kettle;

import org.pentaho.di.core.Condition;
import org.pentaho.di.core.logging.LogLevel;

public class StepDebugLevel implements Cloneable {
  private LogLevel logLevel;
  private int startRow;
  private int endRow;
  private Condition condition;

  public StepDebugLevel() {
    condition = new Condition();
    logLevel=LogLevel.DEBUG;
    startRow=-1;
    endRow=-1;
  }

  public StepDebugLevel( LogLevel logLevel ) {
    this();
    this.logLevel = logLevel;
  }

  public StepDebugLevel( LogLevel logLevel, int startRow, int endRow, Condition condition ) {
    this(logLevel);
    this.startRow = startRow;
    this.endRow = endRow;
    this.condition = condition;
  }

  @Override public StepDebugLevel clone() {
    return new StepDebugLevel(logLevel, startRow, endRow, (Condition) condition.clone() );
  }

  /**
   * Gets logLevel
   *
   * @return value of logLevel
   */
  public LogLevel getLogLevel() {
    return logLevel;
  }

  /**
   * @param logLevel The logLevel to set
   */
  public void setLogLevel( LogLevel logLevel ) {
    this.logLevel = logLevel;
  }

  /**
   * Gets startRow
   *
   * @return value of startRow
   */
  public int getStartRow() {
    return startRow;
  }

  /**
   * @param startRow The startRow to set
   */
  public void setStartRow( int startRow ) {
    this.startRow = startRow;
  }

  /**
   * Gets endRow
   *
   * @return value of endRow
   */
  public int getEndRow() {
    return endRow;
  }

  /**
   * @param endRow The endRow to set
   */
  public void setEndRow( int endRow ) {
    this.endRow = endRow;
  }

  /**
   * Gets condition
   *
   * @return value of condition
   */
  public Condition getCondition() {
    return condition;
  }

  /**
   * @param condition The condition to set
   */
  public void setCondition( Condition condition ) {
    this.condition = condition;
  }
}
