# This repository has been archived. It is now read-only.

# Mapper Framework

A Mapping framework (MF) recursively copies data from one object to another. Typically, these data objects will be of different complex types.

MF is built using [Dozer](http://dozer.sourceforge.net/) framework with several changes as a mapping engine and OpenL Tablets rules framework as a tool which provides convenient mechanism to define conversion rules in declarative way.

The mapper is used any time you need to take one type of Java Bean and map it to another type of Java Bean. It may be used in layered architecture, in distributed systems, for integration with external services and between different frameworks, whenever it’s required to convert or serialize any data model to another similar but different in structure data model.

MF supports simple property mapping, complex type mapping, bi-directional mapping, implicit-explicit mapping, as well as recursive mapping. This includes mapping collection attributes that also need mapping at the element level.

Changes in comparinson with base mapping framework include:
* bi-directional mapping by default
* xpath indexing in collections
* dynamic conditional mapping
* default, required and null/empty mapping enhancements
* multi-source mapping (n-to-1)
* improved hints and convertors

Find implementation details and usage description at [Mapping Framework Reference Guide](https://openl-tablets.org/files/mapping-framework/1.1.19/Mapping%20Framework%20-%20Reference%20Guide.pdf).

To start using Mapping Framework add dependency to your maven pom:

    <dependency>
        <groupId>org.openl.rules</groupId>
        <artifactId>org.openl.rules.mapping.dev</artifactId>
        <version>1.2.2</version>
     </dependency>

## Compatibility matrix

|OpenL Tablets | Mapping framework |
| ------------ | ----------------- |
| 5.22.0 	   | 1.2.2             |
| 5.21.7 	   | 1.1.21 - 1.2.2    |
| 5.20.0 	   | 1.1.17 - 1.2.1    |
| 5.19.1 	   | 1.1.16            |
| 5.19.0 	   | 1.1.15            |
| 5.17.2 	   | 1.1.14            |

