name := "logback-measurements"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.5"

generatorType := "default"

enablePlugins(JmhPlugin)
