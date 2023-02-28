# ensicaen-ia-wasteSorting

LEBOCQ Titouan

## Description du projet

### Exercice description

This project is a waste sorting game. The goal is to sort the waste regrouping them. This waste sorting simulation put
in place a multi-agent system use dto deplace and sort the waste. The waste is represented by a circle and the robots
are represented by a square. The robots can move and sort the waste.

Four types of waste are available:
* MEDIUMORCHID 
* ORANGERED 
* YELLOWGREEN 
* OLIVEDRAB

### My implementation

I have implemented a multi-agent system with a centralised architecture. All agent have access to the same environment
and can communicate with each other. They stop picking up one type of waste when there is only one left on the map. It
implicitly means that the agents are preparing the map to have four different deposit points.

## Installation

### Prerequisites

This application use the maven package manager. You can download it [here](https://maven.apache.org/download.cgi).

### Installation

To install the application, you have to clone the repository and build the project with maven.

```bash
mvn clean install
```
