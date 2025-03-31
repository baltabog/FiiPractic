# FII Practic - From Raw Code to Real Apps: Building with Quarkus



## Getting started
### Prerequisites:
- Min Java17 JDK (Correto-17.0.4)
- Docker (for unix based OS) or WSDL + Rancher Desktop (on Windows OS)
- If using Intellij IDEA install plugins: Quarkus Tools and Quarkus Run Configs 
### First start
Clone project from GitHub repository <br />
Run maven compile: maven clean compile <br />
Check if generated files are marked as source dir (administrator/target/generated-sources/* on admin project and workInProgress/target/generated-sources/* on workInProgress project) <br />
Start applications using existing Run configurations (start_admin / start_wip)

## Test and Deploy - work in progress
Testing of your work will be done automatically when you create a new merge request.
Merging to the GIT master branch can be done only after all tests succeed.

## Description
The project contains three modules that will allow you to understand how / when IT helps manufacturers to create better products.
### Administrator module
Using this module, we can add all equipment/materials/process steps and orders from one manufatoring plant. 
### WorkInProgress module
This module will let us manage what happens (regarding orders and equipment) in one plant (described using Administrator module) in real time.
### Simulator
TODO: add description

## Roadmap
[ ] describe modules<br />
[ ] implement modules (including unit + integration (on DB and in case of WIP module: integration with admin module) tests)<br />
[ ] test products using simulator

## Contributing
- Bogdan B.
- Iulian Muntean
- Dan Ursu
- please add your name before first commit

## Authors and acknowledgment
This project was developed with the help of **CamLine Solutions**, part of  **CamLine Elisa Industriq** company as part of **FII Practic** project.
Main branch contributors:
- Bogdan B.
- Iulian Muntean
- Dan Ursu

## License
For open source projects, say how it is licensed.

## Project status
Work In Progress
