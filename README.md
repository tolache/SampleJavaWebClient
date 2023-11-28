# Sample Java Web Client
A simple web client example for demo purposes.

## Build

```
javac SampleJavaWebClient.java
```

## Use

```
java SampleJavaWebClient $headers $uri $outputFilename $bufferSizeBytes
```

### Example

```
java SampleJavaWebClient "Authorization: Bearer $token" https://example.com/file.dat outfile.dat 4096
```