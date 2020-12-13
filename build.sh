./gradlew -q clean
./gradlew -q distZip
unzip -qq build/distributions/java-hash-benchmark-1.1.0.zip -d build/distributions/
alias jhb="$PWD/build/distributions/java-hash-benchmark-1.1.0/bin/java-hash-benchmark"
