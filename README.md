## Polytechnic of Porto, School of Management and Technology 20/21
<a href="https://www.estg.ipp.pt/"><img src="https://user-images.githubusercontent.com/44362304/94424125-9f4d8a00-0181-11eb-84cb-174d8dbde5ec.png" title="ESTG"></a>

(Click on the above image for more school's details)

## Away-From-Me-Covid
![Away From Me Covid Logo](https://user-images.githubusercontent.com/44362304/107673289-c4a0e780-6c8d-11eb-887e-5f56287ea2bf.png)

### School project developed in Java language using sockets for unit course "Distributed Systems"
This project is intended to implement a small system similar to the Portuguese app StayAwayCovid (https://stayawaycovid.pt/) which aims to assist the country in tracking COVID-19 .
The general idea is that the system should be able to alert the population about exposure to the virus, alerting people whenever they have contact with someone infected, receiving and sending some data about that same population.

For this project was used the population of the sub-region "Tâmega e Vale do Sousa" (located in the north of Portugal):

<img src="https://user-images.githubusercontent.com/44362304/107681656-3cbfdb00-6c97-11eb-981a-56b3935da286.png" width="300" height="300">

## Table of Contents

- [Project Overview](#project_overview)
- [Project Screenshots](#project_screenshots)
- [Usage](#usage)
- [Documentation](#documentation)
- [Project Contributors](#project_contributors)
- [License](#license)

<a name="project_overview"></a>
## Project Oveview
![imagem](https://user-images.githubusercontent.com/44362304/107673660-19dcf900-6c8e-11eb-9b1e-ffbb5d17f614.png)

**(Before reading and for a better comprehension of the project overview description please take a quick look at this small glossary!)**

Glossary:
- **GUI** - **G**uide **U**ser **I**nterface
- **CNI** – **C**entral **N**ode **I**nstructions
- **TCP** – **T**ransmission **C**ontrol **P**rotocol
- **UDP** – **U**ser **D**atagram **P**rotocol

Everything starts from the client's interaction with the **GUI**, where from the moment the client is able to enter the platform, the server accepts its connection (socket) and executes **WorkerThread** (thread responsible for **dealing with each client individually**).

The results of the interactions that the client has with the **GUI** and its information will be in a socket that will be sent via **TCP** protocol to **WorkerThread**.

**WorkerThread** having this information, tells the **CNI** that the client has done a given action on the **GUI** and the **CNI**, knowing what the client has done with the **GUI**, uses a particular **Central Node** method associated with that client action.

The **CNI** sends the result of the **Central Node** method, to **WorkerThread**, and this through the **TCP** protocol, sends that result to **MsgReceiverThread**, which after this thread, will try to launch an alert box in the client's **GUI** with the respective result received .

On the server side there is the **Thread Sender** block, consisting of 2 threads (**BroadcastServerSenderThread** and **MulticastServerSenderThread**) that will be responsible for sending, **temporarily** via **UDP** protocol, datagram packets to the client thread **UDPClientMsgReceiverThread** (thread responsible for receiving the datagram packets with the information relative to the total number of infected and then launch an alert box in the client's **GUI**). 

<a name="project_screenshots"></a>
## Project Screenshots
Some of the project screenshots

| Main Menu | Notification Warning Contact with Close Contact |
| :---: |:---:| 
| <img src="https://user-images.githubusercontent.com/44362304/107680344-a17a3600-6c95-11eb-98bf-97c92ace1970.png" width="450" height="450"> |<img src="https://user-images.githubusercontent.com/44362304/107680339-a0e19f80-6c95-11eb-8827-cc6f2de66987.png" width="450" height="450"> |
| Adding Contact | Covid-19 Test Result |
| <img src="https://user-images.githubusercontent.com/44362304/107680350-a212cc80-6c95-11eb-92ae-5c766569b161.png" width="450" height="450"> | <img src="https://user-images.githubusercontent.com/44362304/107680343-a17a3600-6c95-11eb-86af-d3c98e302895.png" width="450" height="450"> |
| Notification Number People Infected with Covid-19 in a County | Notification Number People Infected with Covid-19 in a Sub-region |
| <img src="https://user-images.githubusercontent.com/44362304/107680346-a212cc80-6c95-11eb-9478-509a449053e6.png" width="450" height="450"> | <img src="https://user-images.githubusercontent.com/44362304/107680349-a212cc80-6c95-11eb-9758-52d9a7098196.png" width="450" height="450"> |

<a name="usage"></a>
## Usage

You must meet the following requirements:
- Having **JDK8** installed
- Having an **IDE** installed (Intellij, Eclipse ...)

After that you must run/execute first the Server class (in Server/Server.java) and then the LoginMenu class (Client/LoginMenu.java), otherwise the application won't run as expected.

<a name="documentation"></a>
## Documentation
To access project javadoc you need to download to a zip format this project and then go to directory lib/javadoc/index.html
<p><a href="https://github.com/Joaodevgit/Away-From-Me-Covid/tree/main/documentation"> Project Documentation Directory </a></p>

<a name="project_contributors"></a>
## Project Contributors
| João Pereira | Paulo da Cunha |
| :---: |:---:| 
| ![João Pereira](https://avatars2.githubusercontent.com/u/44362304?s=200&u=e779f8e4e1d4788360e7478a675df73f219b42b4&v=3)| ![Paulo da Cunha](https://avatars0.githubusercontent.com/u/39674226?s=200&u=5e980e380bf0b9d7a7f821ddcc6fe6112e026ae9&v=4) |
| <a href="https://github.com/Joaodevgit" target="_blank">`github.com/Joaodevgit`</a> | <a href="https://github.com/PauloDevGit" target="_blank">`github.com/PauloDevGit`</a>|

<a name="license"></a>
## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)
- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2021 © João Pereira, Paulo da Cunha.

