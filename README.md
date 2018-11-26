Welcome to Drettuce, a currency exchange application (where all the currency is virtual, you can pretend it's bitcoins).
The main use of this app is to make transfers to other users and to also send requests to them.
All the transactions are recorded in txt files from the pc where the program is run.
By default, when the program is run it will connect to the local host with the credential of a simple app user.
If the connection is not established, the program will ask the user for the database admin's credentials.
If correct credentials are provided, the program will automatically create the schema.


The 1st user is the app admin, his username and password is "admin".
He is the one responsible for creating new users and assigning them roles.

Every higher role has the access of his inferior plus some extra access.
The roles are as follows:
Access level 0: Can make transfers and send requests, checks his own transactions.
Access level 1: Can check other user's transactions.
Access level 2: Can edit other user's transactions.
Access level 3: Can delete other user's transactions.
Access level 4-9: App Admin, can create, edit and delete users.

Made in IntelliJ IDEA Ultimate
Created by Sfinias Ioannis
