# Java Hash Benchmark

`java-hash-benchmark` is a command line tool to benchmark hash algorithms in major Java libraries (e.g. Java, Guava, Apache, etc.) with large-scale streamed data (e.g. a few GB). 

Here is a complete list of implementation providers and algorithms covered by this tool:

* Guava
  * ADLER32
  * CRC32
  * CRC32C
  * MD5
  * MURMUR3_32
  * MURMUR3_128
  * SHA1
  * SHA256
  * SHA384
  * SHA512
  * SIP_HASH_24
* Java
  * ADLER32
  * CRC32
  * CRC32C
  * MD5
  * SHA1
  * SHA256
* Apache
  * MD2
  * MD5
  * SHA1
  * SHA256
  * SHA3_224
  * SHA3_256
  * SHA3_384
  * SHA3_512
  * SHA384
  * SHA512_224
  * SHA512_256
  * SHA512
* jpountz.net
  * XXHASH32
  * XXHASH64

## Getting Started

First, download the [latest release](https://share.jen.ms/java-hash-benchmark/java-hash-benchmark-1.1.0.zip) and unzip the file:

```bash
unzip java-hash-benchmark-1.1.0.zip
```

Then, create an alias `jhb`:

```bash
alias jhb='./java-hash-benchmark-1.1.0/bin/java-hash-benchmark'
```

Now you can run the command `jhb` with the following options:

* `-d, --data-size=<dataSize>`: *[Optional]* Total number of bytes to be fed into each of the hash algorithms; the default value is 1073741824 (1 GB) if this option is not provided.
* `-b, --buffer-size=<bufferSize>`: *[Optional]* Number of bytes to be fed into each hash algorithm in a single iteration; the default value is 4096 if this option is not provided.
* `-o, --output=<outputCsvPath>`: *[Optional]* Path to the output CSV file. If this option is not provided, no output file will be generated. If a file already exists at this path, the result (without headers) will be appended to the end of the existing file.

For example, if you want to run the hash algorithms with:

* 700 MB input data
* 2048 KB buffer size
* Output file to `output.csv`

, simply run the following command:

```bash
jhb -d 734003200 -b 2048 -o output.csv
```

An example output CSV file will be in the following format:

|Provider|Algorithm|Data Size|Buffer Size|Duration (ms)|Speed (MB/s)|Hash Size (bits)|Hash Result                     |
|--------|---------|---------|-----------|-------------|------------|----------------|--------------------------------|
|GUAVA   |MURMUR3_32|700.00 MB|2048       |650          |1076.923    |32              |923ddc22                        |
| ...
|JAVA    |ADLER32  |700.00 MB|2048       |64           |10937.500   |32              |01005e90                        |
| ...
|APACHE  |MD5      |700.00 MB|2048       |1255         |557.769     |128             |7fe5ca2a051d6dbb9ef191fbee0af98c|
| ...
|JPOUNTZ |XXHASH32 |700.00 MB|2048       |140          |5000.000    |32              |6b4d9737                        |
|JPOUNTZ |XXHASH64 |700.00 MB|2048       |68           |10294.118   |64              |0f1741b97779bd65                |

For Windows users, please use the `./java-hash-benchmark-1.1.0/bin/java-hash-benchmark.bat` executable instead.

## Development Guide

You should only continue reading if you wish to add more algorithms to this tool.

### Step 1: Add new entries to `HashAlgorithm` and `ProviderName`.

These two enums represent and hash algorithms and implementation providers. If you wish to add new hash algorithms or new implementation providers to this tool, you may need to add your entries into:

* [`ms.jen.hashing.benchmark.provider.HashAlgorithm`](https://sourcegraph.com/github.com/jenqh/java-hash-benchmark/-/blob/src/main/java/ms/jen/hashing/benchmark/provider/HashAlgorithm.java)
* [`ms.jen.hashing.benchmark.provider.Providername`](https://sourcegraph.com/github.com/jenqh/java-hash-benchmark/-/blob/src/main/java/ms/jen/hashing/benchmark/provider/ProviderName.java)

For example:

```java
public enum HashAlgorithm {
  ADLER32(32),
  CRC32(32),
  // ...
  // Add your new hash algorithm here
  MY_ALGORITHM(128); // 128 is the number of bits in the hash result
}
```

```java
public enum ProviderName {
  GUAVA,
  JAVA,
  // ...
  // Add your new provider name here
  MY_PROVIDER,
}
```

### Step 2: Create a `HashWorker` for the new hash algorithm

The [`ms.jen.hashing.benchmark.worker.HashWorker`](https://sourcegraph.com/github.com/jenqh/java-hash-benchmark/-/blob/src/main/java/ms/jen/hashing/benchmark/worker/HashWorker.java) is an interface that represents a single worker that performs the hash algorithm on streamed input data. By design, a new instance should be created whenever a new stream of data needs to be hashed, and destroyed after the hashing result of the input data is retrieved. There are two APIs in this interface:

```java
public interface HashWorker {

  /** Update the worker when more streamed data is available. */
  void update(byte... bytes);

  /** Gets the hash result when all data has been fed to {@link #update(byte...)}. */
  HashCode getResult();
}
```

Note that the `HashCode` class is defined in Guava (`com.google.common.hash.HashCode`).

### Step 3: Implement and register `HashServiceProvider` for the new hash service provider

If you have added a new entry into `ProviderName`, you need to create a corresponding implementation of the [`ms.jen.hashing.benchmark.provider.HashServiceProvider`](https://sourcegraph.com/github.com/jenqh/java-hash-benchmark/-/blob/src/main/java/ms/jen/hashing/benchmark/provider/HashServiceProvider.java) interface:

```java
public interface HashServiceProvider {

  /** Returns true if the provider supports this algorithm. */
  boolean hasHashAlgorithm(HashAlgorithm algorithm);

  /** Creates a {@link HashWorker} for the given {@link HashAlgorithm}. */
  HashWorker createHashWorker(HashAlgorithm algorithm);
}
```

After the new implementation (e.g. `MyNewHashServiceProvider`) is created , remember to register it to [`ms.jen.hashing.benchmark.core.Candidate.PROVIDERS`](https://sourcegraph.com/github.com/jenqh/java-hash-benchmark/-/blob/src/main/java/ms/jen/hashing/benchmark/core/Candidate.java):

```java
private static final ImmutableMap<ProviderName, HashServiceProvider> PROVIDERS = 
    ImmutableMap.<ProviderName, HashServiceProvider>builder()
        .put(ProviderName.GUAVA, GuavaHashServiceProvider.INSTANCE)
        // ...
        // Register your HashServiceProvider here
        .put(ProviderName.MY_PROVIDER, MyNewHashServiceProvider.INSTANCE)
        .build();
```

### Step 4: Let the tool run your algorithms

Now your newly created algorithms are ready to be used for benchmarking. 

Simply navigate to the bottom of the [`ms.jen.hashing.benchmark.Main`](https://sourcegraph.com/github.com/jenqh/java-hash-benchmark/-/blob/src/main/java/ms/jen/hashing/benchmark/Main.java) class, and add a few combinations of your new `ProviderName` and `HashAlgorithm` to the `createBuilderWithCandidates()` method:

```java
return Benchmark.newBuilder()
    // Guava
    .addCandidate(ProviderName.GUAVA, HashAlgorithm.ADLER32)
    .addCandidate(ProviderName.GUAVA, HashAlgorithm.CRC32)
    // ...
    // Add your new combinations here
    .addCandidate(ProviderName.MY_PROVIDER, HashAlgorithm.MY_ALGORITHM);
```

### Step 5: Build the executable

#### Manual build

You can use the default Gradle application plugin to build the tool:

```bash
./gradlew clean && ./gradlew distZip
```

Then, the zip that contains the executables will be found in the `./build/distributions` directory.

#### Automatic build, unzip and alias

Just run the following command:

```bash
source build.sh
```
Then, you can try out the newly built executable with the same `jhb` command.
