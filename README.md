[![License](https://img.shields.io/:license-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.org/atbashEE/atbash-json-smart.svg?branch=master)](https://github.com/atbashEE/atbash-json-smart)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/be.atbash.json/octopus-json-smart/badge.svg)](https://maven-badges.herokuapp.com/maven-central/be.atbash.json/octopus-json-smart)

# atbash-json-smart

Small library capable of converting Java instances to JSON and back, for Java 8, 11 and 17 inspired on [JSON Smart v2](https://github.com/netplex/json-smart-v2).

With the standardization of this functionality in JSON-B, it advised to use a JSON-B implementation unless you are outside a Jakarta runtime and want some small and fast JSON handling.

## Requirements

Runs on JDK 8, JDK 11 ClassPath, and JDK 17 ClassPath.

## Configuration

Add artifact to pom.xml file.

----
    <dependency>
        <groupId>be.atbash.json</groupId>
        <artifactId>octopus-json-smart</artifactId>
        <version>1.0.0</version>
    </dependency>
----


## Reading JSON

Converts the "\<json>" string to instance of T.

----
    T JSONValue.parse("<json>", Class<T>);
----

## Converting to JSON

Converts the POJO T to a JSON String

----
    String JSONValue.toJSONString(T);
----

## Customizing JSON reading/creation

* Implement the _JSONAware_ interface
* Define custom encoders and writers with _@MappedBy_ and custom _CustomJSONEncoder_, _CustomBeanJSONEncoder_ and/or _Writer_.
* Define a custom Encoder which also implement _JSONEncoderBuilder_ interface to have a builder like pattern for encoding JSON to Java.  
  Extend _CustomBeanBuilderJSONEncoder_ and define it as encoder at @MappedBy.  
