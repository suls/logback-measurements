# Benchmarking logback using sbt-jmh

I was wondering if having a variable in the pattern supplied to 
`PatternLayoutEncoder` would have any effect on performance or not.

```
Benchmark                              Mode  Cnt   Score   Error   Units
TestLogbackPattern.baseline           thrpt   20  12.067 ± 0.786  ops/us
TestLogbackPattern.variableInPattern  thrpt   20  12.232 ± 0.706  ops/us
```

## Conclusion

`PatternLayoutEncoder` seems not to be affected by buildin up the 
pattern programatically or not.

## Reproducing

Run `jmh:run -i 20 -wi 20 -f1 -t1` from sbt console
