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
and can communicate with each other. At the start of the simulation, the environment is filled with waste of random
sizes. The robots are randomly placed in the environment. The robots can move and sort the waste but only one unit by
one unit.

All non busy agent will move towards the nearest waste and pick it up.

All busy agent will move towards the nearest sorting area (DepositPoint) and drop the waste. By default all drop point
are located at the center of the environment.

## Installation

### Prerequisites

This application use the maven package manager. You can download it [here](https://maven.apache.org/download.cgi).

### Installation

To install the application, you have to clone the repository and build the project with maven.

```bash
mvn clean install
```
