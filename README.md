<!--                                                                           -->
<!--  Copyright 2013 Xebia and Séven Le Mesle                                  -->
<!--                                                                           -->
<!--  Licensed under the Apache License, Version 2.0 (the "License");          -->
<!--  you may not use this file except in compliance with the License.         -->
<!--  You may obtain a copy of the License at                                  -->
<!--                                                                           -->
<!--           http://www.apache.org/licenses/LICENSE-2.0                      -->
<!--                                                                           -->
<!--  Unless required by applicable law or agreed to in writing, software      -->
<!--  distributed under the License is distributed on an "AS IS" BASIS,        -->
<!--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. -->
<!--  See the License for the specific language governing permissions and      -->
<!--  limitations under the License.                                           -->
<!--                                                                           -->

[![Build Status](https://travis-ci.org/xebia-france/selma.png?branch=master)](https://travis-ci.org/xebia-france/selma)

[![S3lm4 logo](https://raw.github.com/xebia-france/selma/master/resources/logo-v6.png)](http://xebia-france.github.io/selma/)

## Selma Java bean mapping at compile time !

# What is S3lm4 ?

S3lm4 say Selma, stands for ***Stupid Simple Staticaly Linked Mapper***.
In fact it is on one side an Annotation Processor that generate Java code to handle the mapping from field to field at compile time. On the other side, it is a Runtime library to instantiate and invoke the generated Mapper.


# How does it works ?

First add selma-processor as a provided dependency and selma as a compile dependency to your build.
Then, define a Mapper interface describing the mapping you want:

```java
@Mapper
public interface SelmaMapper {

    OutBean asOutBean(InBean in);

}
```

Then ? Well just use the generated Mapper:

```java

    SelmaMapper mapper = Selma.mapper(SelmaMapper.class);

    OutBean res = mapper.asOutBean(in);

```

And voilà !

Visit our site: (http://xebia-france.github.io/selma/)

## Features

* Generate code for mapping bean to bean matching fields to fields
** Support for nested bean
** Bean should respect Java property convention
* Custom fieldname to fieldname mapping
* Maps Enum using identical values with default value
* Maps Collection any to any
* Maps Map any to any
* Use strict memory duplication for all fields
* Support for SourcedBeans to instantiate beans is out of the box
* Support Type to Type custom mapping using custom mapping methods
* Gives full feedback at compilation time
* Break build when mapping does not work Say good bye to mapping errors in production

## Usage

First add selma and selma-processor to your pom dependencies:
```xml
        <!-- scope provided because the processor is only needed for the compiler -->
        <dependency>
            <groupId>fr.xebia.extras</groupId>
            <artifactId>selma-processor</artifactId>
            <version>0.3</version>
            <scope>provided</scope>
        </dependency>

        <!-- This is the only real dependency you will have in youre binaries -->
        <dependency>
            <groupId>fr.xebia.extras</groupId>
            <artifactId>selma</artifactId>
            <version>0.3</version>
        </dependency>
```

Then, as I said earlier, build youre interface with @Mapper annotation and enjoy.

Checkout the example module to have a deeper look.

**Help needed, please report issues and ask for features :)**

