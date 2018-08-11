# DSAB data loader

##Introduction
This library can be used to load and parse information from the website of the Deutscher Sportautomatenbund e.V. (DSAB) (http://www.dsab-vfs.de).
The DSAB organizes electronic darts events in Germany. This includes nation-wide tournaments as well as amateur and professional leagues.

The results of the leagues in Germany are published on the DSAB website (http://www.dsab-vfs.de). Multiple websites for regional associations also publish the results within their region. Usually the information about a certain leagues and the results requires a couple of clicks and data is presented in multiple formats (e.g. HTML and PDF).
The library provided here allows to load the data from the DSAB website, it prepares it and offers it in a structured way. The DSAB website offers the information using REST so that the access is quite easy and can be derived from the information received.

##Source code
The source code of the library is made available on GitHub: https://github.com/senvB/dsabDataLoader
The library is released under the terms of the GNU Affero General Public License (version 3 or later).

###Dependencies
The code makes use of the following libraries.

- JSON marshalling and unmarshalling
  - com.squareup.moshi:moshi
- IO utils
  - commons-io:commons-io
  - org.apache.commons:commons-lang3
- HTML parsing
  - org.jsoup:jsoup
- PDF parsing
  - com.itextpdf:itextg
- Logging
  - org.slf4j:slf4j-api
  -org.slf4j:slf4j-simple
- Testing of the code
  - junit:junit:4.12
  - com.tngtech.java:junit-dataprovider
  - org.mockito:mockito-all
 

##Data model
The data model offered by the library (after parsing the data) is explained in the GitHub wiki (https://github.com/senvB/dsabDataLoader/wiki/Data-model) and also shown in the resources folder.

![alternativer Text](src/main/resources/DsabDataLoader-DataModel.jpg "Data model")

##Android app using this library
It is used to read actual (and past) scores for all Darts leagues maintained by the DSAB. A free android app using this library is available on http://dartsviewer.senv.de. The source code of the app is also offered on GitHub.

##About the author
I am playing electronic darts as one of my hobbies. This project brings darts in combination with my second hobby: coding. Whenever there is some sparetime I try to code a little bit and learn new stuff.
Living currently in Berlin you may also find my name in the players list when using the related Android app.

In case you would like to use the libary or the app I would be happy to receive a message from you. Also if you would like to contribute or have a nice idea of a new feature, then please don not hesitate to contact me. This also counts for any kind of bugs you encounter when using my code.``

Any kind of feedbeck is welcome under <a href="dartsviewerfeedback@senv.de">dartsviewer@senv.de</a>
