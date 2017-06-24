# Lailaps

**Lailaps** is an application aimed to simplify the download process for documents uploaded to the _HSHL_ learning management system. 
It is completely written in Java and hence usable on different systems.
Its easy to use GUI allows for fast and efficient downloading which would otherwise take several minutes of clicking through course pages.
Also this is my 6th semester software project for the _HSHL_.

## Features

* Easy and fast download of documents from the _HSHL_ learning management system
* Multi-threaded downloading
* Selection of terms you want to download
* Multi-Platform usage
* English and German Version depending on the language settings of your OS
* Remembers the last successfully logged in user name for even faster access
* Coloured indicator for new / already existing / failed downloads to differentiate them easily
* Skipping of already downloaded files

Please note that at no point any password information will be stored on your device or on any server.
Your entered credentials will be directly submitted to the _HSHL_ server only.

## Getting Started

This section will get you covered when you want to get the application running.

### Prerequisites

You will need at least Java version 1.8 or higher to be installed on your computer.
To check which version you have currently installed type the following in your command prompt

```
java -version
```

If you don't have Java installed yet, don't worry. You can grab it here: [Download Java 8.](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Download the precompiled jar

If you don't want to compile the source code yourself or you don't know how to do it, here is a [download link](http://theovier.com/download) to an already
compiled version of the program. Just download the .jar file and execute it by double clicking on it.
Alternatively you can open it using the command prompt

```
java -jar C:\\PathToFile\lailaps.jar
```

### Compile the source yourself

If you don't want to wait for new releases you can compile the source code yourself.
As this is a maven project you need to make sure to have the java build management tool _maven3_ installed.
To check your maven version type

```
mvn -v
```

it should display something along this lines

```
Apache Maven 3.5.0 (ff8f5e7444045639af65f6095c62210b5713f426; 2017-04-03T21:39:06+02:00)
```

If you installed maven just clone the repository or download the .zip and navigate to the pom.xml.
Then simply run 

```
mvn package
```

and check your /target folder for the newly generated jar file.


## Author

Theo Harkenbusch [@theovier](https://github.com/Theovier)


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Miscellaneous

The name **Lailaps** is a reference to the Greek mythological dog.
This dog never failed to catch what she was hunting and this piece of software was also
designed with the thought in mind to never miss a document which is available for download.

## Acknowledgments

Thanks to everyone who helped to build this app!

* Simon for very early testing
* Kai for testing
* Marvin for testing / ideas
* Dominik for discussing code snippets
* Claire for testing and motivation
