package org.suls.logback_measurement


import java.util.concurrent.TimeUnit

import ch.qos.logback.classic.{Level, LoggerContext}
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import ch.qos.logback.core.rolling.RollingFileAppender
import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Mode, OutputTimeUnit, Scope, State}
import org.slf4j.LoggerFactory

@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Array(Mode.Throughput))
@State(Scope.Benchmark)
class TestLogbackPattern {

  def init(name: String, variable: Option[String] = None): Unit = {
    val logCtx = LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]

    val logEncoder = new PatternLayoutEncoder()
    logEncoder.setContext(logCtx)

    variable.fold(logEncoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} aKey=aValue %-5level - %msg%n")) {
      v: String => logEncoder.setPattern("%%-12date{YYYY-MM-dd HH:mm:ss.SSS} %s %%-5level - %%msg%n".format(v))
    }

    logEncoder.start()

    val logFileAppender = new RollingFileAppender[ILoggingEvent]()
    logFileAppender.setContext(logCtx)
    logFileAppender.setName(name)
    logFileAppender.setEncoder(logEncoder)
    logFileAppender.setAppend(true)
    logFileAppender.setFile("/dev/null")

    val log = logCtx.getLogger(name)
    log.setAdditive(false)
    log.setLevel(Level.INFO)
    log.addAppender(logFileAppender)
  }


  init("baseline")
  val baselineLogger = LoggerFactory.getLogger("baseline")

  init("variableInPattern", variable = Some("aKey=aValue"))
  val variableInPatternLogger = LoggerFactory.getLogger("variableInPattern")

  @Benchmark
  def baseline(): Unit = {
    baselineLogger.info("this is a test")
  }

  @Benchmark
  def variableInPattern(): Unit = {
    variableInPatternLogger.info("this is a test")
  }

}
