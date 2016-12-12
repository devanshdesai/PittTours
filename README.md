# PittTours

## Running the Driver
* Copy repository into working directory and start sqlplus
* Run `start schema.sql`
* To insert all generated data into the tables, run `start data.sql`
* Lastly, exit sqlplus, compile the Driver.java file, and run it
* If instead of inserting all the data, you'd prefer to leave out the generated reservation data, skip running `data.sql` and run operation 6 in the administrator interface inside the driver, which loads data from the included CSV files
* To test all functions of the Driver program, you can run operation 8 in the administrator interface. It will insert a customer, display info, show flight information using all of querying for flights, add a reservation, show the reservation, and finally, purchase the ticket.
