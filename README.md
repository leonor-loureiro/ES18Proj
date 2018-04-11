# Adventure Builder

To run tests execute: mvn clean install

To see the coverage reports, go to <module name>/target/site/jacoco/index.html.


|   Number   |          Name           |            Email                     |   Name GitHUb   |  Team  |
| ---------- | ----------------------- | ------------------------------------ | --------------- | ------ |
|   84736    | Leonor Loureiro         | leonor.filipa@tecnico.ulisboa.pt     | leonor-loureiro |   1    |
|   84709    | Diogo Andrade           | diogo.freire.dandrade@gmail.com      | dfAndrade       |   1    |
|   84698    | André Fonseca           | andre.filipe.alves.fonseca@ist.utl.pt| Anfifo          |   1    |
|   84747    | Miguel Viegas           | miguel.s.viegas@tecnico.ulisboa.pt   | JellyWellyBelly |   2    |
|   84702    | Antonio Terra           | antonio.terra@tecnico.ulisboa.pt     | wimpytoaster    |   2    |
|   84774    | Tomás Carrasco          | tomasbccarrasco@gmail.com            | CarrascoII      |   2    |

- **Team 1:**
	Responsible for activity, hotel and car modules
	
- **Team 2:**
	Responsible for broker and tax modules
	
This readme must be read together with the ExcelSheet Entrega3-Dep.xlsx.
	
Responsibilities:

	Team1:
		dfAndrade
			[CV2]		Make Vehicle,Car and Motorcycle attributes persistent
			[CRA1]		Make RentACar persistent
			[CRA2]		Make RentACar attributes persistent
			[CR1]		Make Renting persistent
			[CR2]		Make Renting attributes persistent
			[CRAVR]		Create RentACar---Vehicle relationship
			[CRVR]		Create Renting---Vehicle relationship
			
	
		leonor-loureiro
			[AAO1]		Make ActivityOffer attributes persistent
			[AAP1]		Make ActivityProvider attributes persistent
			[AB1]		Make Booking atributes persistent
			[AP1]		Make Processor persistent
			[AP2]		Make Processor attributes persistent
			[ABPR]		Create Booking---Processor relationship
			[CV1]		Make Vehicle,Car and Motorcycle persistent
					
	
		Anfifo
			[CP1]		Make Car Processor persistent
			[CP2]		Make Car Processor attributes persistent
			[CRPR]		Create Renting---Processor relationship
			[H1]		Make Hotel atributes persistent
			[HB1]		Make Booking atributes persistent
			[HP1]		Make Processor persistent
			[HP2]		Make Processor attributes persistent
			[HBPR]		Create Booking---Processor relationship

		Team 1 Dependecies
			Activity does not have any external-module dependencies and there is only one person working on it. (leonor-loureiro)
			Activity does not have any external-module dependencies and there is only one person working on it. (Anfifo)
			Team 1 has it's dependencies (car module) solved in the schedule.
			
	Team2:		
		CarrascoII
			[TIR1]		Make IRS persistent
			[TTP1]		Make TaxPayer, Seller and Buyer persistent
			[TIR2]		Make IRS attributes persistent
			[TTPIRR]	Create TaxPayer---IRS relationship
			[TITIRR]	Create ItemType---IRS relationship
			[TBIR]		Create Buyer---Invoice relationship
			[TI2]		Make Invoice attributes presistent
			
		wimpytoaster
			[TI1]		Make Invoice presistent
			[TIT1]		Make ItemType persistent
			[TITIR]		Create ItemType---Invoice relationship
			[TIT2]		Make ItemType attributes persistent
			[TSIR]		Create Seller---Invoice relationship
			[TTP2]		Make TaxPayer, Seller and Buyer attributes persistent
			
		JellyWellyBelly
			[BC1]		Make Client persistent
			[BC2]		Make Client attributes persistent
			[BCBR]		Create Client---Broker relationship
			[BB1]		Make Broker attributes persistent
			[BBR1]		Make BulkRoomBooking attributes persistent
			[BA1]		Make Adventure attributes persistent
			[BACR]		Create Adventure---Client relationship
			
		Team 2 Dependecies
			The dependencies in tax module forced team 2's development (of only this module) to be separated in to 4 different stages:
			
			Stage 1 	
			[TIR1]		Make IRS persistent
			[TTP1]		Make TaxPayer, Seller and Buyer persistent
			[TI1]		Make Invoice presistent
			[TIT1]		Make ItemType persistent
						
			Stage 2	
			[TIR2]		Make IRS attributes persistent
			[TTPIRR]	Create TaxPayer---IRS relationship
			[TITIRR]	Create ItemType---IRS relationship
			[TITIR]	Create ItemType---Invoice relationship
			[TIT2]	Make ItemType attributes persistent
						
			Stage 3  
			[TBIR]		Create Buyer---Invoice relationship
			[TSIR]		Create Seller---Invoice relationship
						
			Stage 4	
			[TI2]		Make Invoice attributes presistent
			[TTP2]		Make TaxPayer, Seller and Buyer attributes persistent
			
			Broker does not have any external-module dependencies and there is only one person working on it. (JellyWellyBelly)
			Team 2 has it's dependencies (car module) solved in the schedule.
			The worker JellyWellyBelly will not have a fixed schedule because of volatile extra-curricular activities. 
		