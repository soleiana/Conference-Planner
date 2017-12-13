Implement following RESTful web service application according to the following specifications:

Database specification:
1. Conference
    Name        | Conference name, max length 150 characters
    Date time   | Conference date & time
2. Participant 
    Full name   | Person full name
    Birth date  | Date of birth
3. Conference room
    Name        | Room name, like “M/S Baltic Queen conference”
    Location    | M/S Baltic Queen
    Max seats   | 124
    
RESTful web service specification:
1. Conference management
 - Create new conference
 - Cancel conference
 - Check conference room availability (based on registered participants and conference room max seats)
2. Conference participant management
 - Add participant to conference
 - Remove participant from conference
