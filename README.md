-# Adventure Builder

To run tests execute: mvn clean install

To see the coverage reports, go to module_name/target/site/jacoco/index.html.


|   Number   |          Name           |            Email                     |   Name GitHUb   | Grupo |
| ---------- | ----------------------- | ------------------------------------ | --------------- | ----- |
|   84736    | Leonor Loureiro         | leonor.filipa@tecnico.ulisboa.pt     | leonor-loureiro |   1   |
|   84709    | Diogo Andrade           | diogo.freire.dandrade@gmail.com      | dfAndrade       |   1   |
|   84698    | André Fonseca           | andre.filipe.alves.fonseca@ist.utl.pt| Anfifo          |   1   |
|   84747    | Miguel Viegas           | miguel.s.viegas@tecnico.ulisboa.pt   | JellyWellyBelly |   2   |
|   84702    | Antonio Terra           | antonio.terra@tecnico.ulisboa.pt     | wimpytoaster    |   2   |
|   84774    | Tomás Carrasco          | tomasbccarrasco@gmail.com            | CarrascoII      |   2   |


Team 1: Broker
        Implement method for car interface

Team 2: Hotel, Car & Tax

Responsibilities:

	dfAndrade 
		Create the entity Client in broker (replace IBAN)
		Associate two NIFs to broker (seller and buyer)
		Add car interface

	leonor-loureiro
		Create state rent vehicle
		Move state payment to before confirmation
		Add tax interface

	Anfifo
		Submit invoice to tax module in state payment
		Confirmation state moves to undo if some of the modules did not have payment references or did not sent invoice to tax
		Submit cancel invoice to tax in state undo
		
	CarrascoII
		Add NIF and IBAN
		Processes payment in bank
		Manage queues of pending invoices
		
	wimpytoaster
		Add bank interface
		Add tax interface
		Submit invoice to tax module
		Submit cancel payment to bank 

	JellyWellyBelly
		Add price to a room/vehicle
		Submit cancel invoice to tax
		Handle errors in cancel invoice to tax
		Handle errors in cancel payment to bank
		
		
		
		
		
		
		
		
		
		
		
		
		
		