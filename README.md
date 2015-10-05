Generic DataSet Framework in Java
=====================
[![Travis CI Build Status](https://travis-ci.org/decebals/dataset.png)](https://travis-ci.org/decebals/dataset)
[![Coverage Status](https://coveralls.io/repos/decebals/dataset/badge.svg?branch=master&service=github)](https://coveralls.io/github/decebals/dataset?branch=master)
[![Maven Central](http://img.shields.io/maven-central/v/ro.fortsoft.dataset/dataset.svg)](http://search.maven.org/#search|ga|1|ro.fortsoft.dataset)

[![Issue Stats](http://www.issuestats.com/github/decebals/dataset/badge/issue?style=flat)](http://www.issuestats.com/github/decebals/dataset)
[![Issue Stats](http://www.issuestats.com/github/decebals/dataset/badge/pr?style=flat)](http://www.issuestats.com/github/decebals/dataset)

It's an open source (Apache License) Generic DataSet Framework in Java, with minimal dependencies and a quick learning curve.     
The goal of this project is to create a DataSet concept similar with ResultSet from JDBC but with support for many data stores.

Modules
-------------------
Practically this is a microframework and the aim is to keep the core simple but extensible.
For now are available these modules:
- dataset-core (defines the core interfaces and classes; add `InMemoryDataSet` and `ResultSetDataSet` implementations)
- __dataset-csv__ (add `CsvDataSet`)
- __dataset-json__ (add `JsonDataSet`)
- __dataset-xls__ (add `XlsDataSet`)
- __dataset-xml__ (add `XmlDataSet`)

Using Maven
-------------------
In your pom.xml you must define the dependencies to DataSet artifacts with:

```xml
<dependency>
    <groupId>ro.fortsoft.dataset</groupId>
    <artifactId>dataset-core</artifactId>
    <version>${dataset.version}</version>
</dependency>
```

where ${dataset.version} is the last dataset version.

You may want to check for the latest released version using [Maven Search](http://search.maven.org/#search%7Cga%7C1%dataset)

How to use
-------------------
It's very simple to add dataset in your application:

```java
// create a dataset from a csv file
DataSet dataset = new CsvDataSet.Builder(getResourceStream("test.csv")).build();

// retrieves the dataset meta data
DataSetMetaData dataSetMetaData = dataSet.getMetaData();

// "first_name","last_name","company_name","address","city","county","state","zip","phone1","phone2","email","web"
int fieldCount = dataSetMetaData.getFieldCount();
assertEquals(12, fieldCount);
assertEquals("first_name", dataSetMetaData.getFieldName(0));
assertEquals("web", dataSetMetaData.getFieldName(fieldCount - 1));

// iterate the dataset
int count = 1;
while (dataSet.next()) {
    count++;
    // show the current row number
    System.out.println(dataSet.getCursorPosition());
    System.out.println(>>>>>>>>>>>>>>>>>>>>>>>>>>>>>);
    // show all fields
    for (int i = 0; i < fieldCount; i++) {
        System.out.println(dataSet.getObject(i));
    }
    System.out.println(<<<<<<<<<<<<<<<<<<<<<<<<<<<<<);
}
assertEquals(500, dataSet.getCursorPosition());
assertEquals(500, count);
```

The `test.csv` file looks like:
```
"first_name","last_name","company_name","address","city","county","state","zip","phone1","phone2","email","web"
"James","Butt","Benton, John B Jr","6649 N Blue Gum St","New Orleans","Orleans","LA",70116,"504-621-8927","504-845-1427","jbutt@gmail.com","http://www.bentonjohnbjr.com"
"Josephine","Darakjy","Chanay, Jeffrey A Esq","4 B Blue Ridge Blvd","Brighton","Livingston","MI",48116,"810-292-9388","810-374-9840","josephine_darakjy@darakjy.org","http://www.chanayjeffreyaesq.com"
"Art","Venere","Chemel, James L Cpa","8 W Cerritos Ave #54","Bridgeport","Gloucester","NJ","08014","856-636-8749","856-264-4130","art@venere.org","http://www.chemeljameslcpa.com"
"Lenna","Paprocki","Feltz Printing Service","639 Main St","Anchorage","Anchorage","AK",99501,"907-385-4412","907-921-2010","lpaprocki@hotmail.com","http://www.feltzprintingservice.com"
"Donette","Foller","Printing Dimensions","34 Center St","Hamilton","Butler","OH",45011,"513-570-1893","513-549-4561","donette.foller@cox.net","http://www.printingdimensions.com"
"Simona","Morasca","Chapman, Ross E Esq","3 Mcauley Dr","Ashland","Ashland","OH",44805,"419-503-2484","419-800-6759","simona@morasca.com","http://www.chapmanrosseesq.com"
```

How to build
-------------------
Requirements:
- [Git](http://git-scm.com/)
- JDK 7 (test with `java -version`)
- [Apache Maven 3](http://maven.apache.org/) (test with `mvn -version`)

Steps:
- create a local clone of this repository (with `git clone https://github.com/decebals/dataset.git`)
- go to project's folder (with `cd dataset`)
- build the artifacts (with `mvn clean package` or `mvn clean install`)

After above steps a folder _dataset/target_ is created and all goodies are in that folder.

Versioning
------------
DataSet will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org.
