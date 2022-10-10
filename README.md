# IDS-AP Software
Software components and tools to interchange and validate Application Profiles.   
An instance is deployed here: https://ids-ap.fokus.fraunhofer.de/

# Usage

The DataspaceConnector (DSC) can be used to create example connector self-descriptions. 
piveau-metrics-validating-shacl is a simple service that allows to validate RDF payloads 
against predefined SHACL shapes. piveau-metrics-validating-shacl-ui is a corresponding 
web frontend.

## Build and start the DSC

In root of repository, enter the following in a *separate* command line window:

```
cd DataspaceConnector
mvn package -DskipTests
java -jar target/dataspaceconnector.jar
```

## Build and start the SHACL Validator

In root of repository, enter the following in a *separate* command line window:

```
cd piveau-metrics-validating-shacl
mvn package -DskipTests
java -jar target/validating-shacl.jar
```

Note: The DSC runs on port 8888, the SHACL Validator runs on port 8080

## Build and start the SHACL Validator UI

In root of repository, enter the following in a *separate* command line window:

```
cd piveau-metrics-validating-shacl-ui
npm install
npm run dev
```


## Query endpoints

The only endpoint of the DSC that currently implements the automatic JSONLD-testing is the self description endpoint `https://localhost:8888/api/connector`. It has been duplicated under `https://localhost:8888/api/connectorbad` that generates a corrupted self description that does not conform to the IDS Base Application Profile specified in the SHACL Shapes.

# How to change the code

## Add application profile

New APs can be placed in a separate folder under `ids-ap-software/piveau-metrics-validating-shacl/src/main/resources/rdf/shapes` as SHACL shapes.
In code, a mapping must be extended that maps an AP's identifier to the folder location:

* Open `ids-ap-software/piveau-metrics-validating-shacl/src/main/kotlin/io/piveau/validating/DCATAPShapes.kt`
* Create a new object that contains the loaded SHACL shapes. Use `IDSAPBASIC` as a reference.
* Add the new AP to the mapping functions `String.findShapesModel()` and `String.findShapes()`.
* The string that is used for the lookup in the mapping functions will be the string that is supplied in the query parameter `shapesModel` to select the AP.

Example: `POST http://localhost:8080/shacl/validation/report?shapesModel=idsbase`
Needed headers: `Content-Type: application/ld+json` 

## Modify validation request in DSC

The function of interest is `checkRDF(String ap, String rdf)` in file `ids-ap-software/DataspaceConnector/src/main/java/io/dataspaceconnector/controller/CheckRDF.java`

Parameters:
* `ap`: Application profile to check. Is supplied as query parameter `shapesModel`, i.e. is the string used in the SHACL lookup in the Validator
* `rdf`: String of JSONLD that needs to be checked

Equivalent curl request: ```curl -X POST http://localhost:8080/shacl/validation/report?shapesModel=<ap> -H "Content-Type: application/ld+json" -d <rdf>```

## Edit ontology

To validate SHACL, the IDS ontology needs to be loaded as well to make use of inference. For example, information about subclass-relationships is vital to decide wether a mandatory requirement is fulfilled.

The ontology is placed under `ids-ap-software/piveau-metrics-validating-shacl/src/main/resources/rdf/shapes/ids-base/4.1.0/ids-ontology.ttl` as a single file serialization. It is loaded in line `23` in `ids-ap-software/piveau-metrics-validating-shacl/src/main/kotlin/io/piveau/validating/DCATAPShapes.kt
