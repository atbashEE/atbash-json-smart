[![License](https://img.shields.io/:license-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.org/atbashEE/atbash-json-smart.svg?branch=master)](https://github.com/atbashEE/atbash-json-smart)

# atbash-json-smart

Small library capable of converting Java instances to JSON and back, for Java 7 based on [JSON Smart v2](https://github.com/netplex/json-smart-v2).

With the standardization of this functionality in JSON-B, it advised to use a JSON-B implementation when using Java 8.

## Configuration

Add artifact to pom.xml file.

----
    <dependency>
        <groupId>be.atbash.json</groupId>
        <artifactId>octopus-json-smart</artifactId>
        <version>0.9.0</version>
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
