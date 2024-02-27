A CRUD app in Spring Boot. I decided to build a Christian blog as an example.

Configuring the uploads folder to store user profile pictures was a different process in Spring Boot
from what I would have expected, so this was informative. 

You'll need a secret.properties file in the resources folder to configure the H2 database and the 
email address for smtp. It should look like this:
'''
spring.datasource.username={blah}
spring.datasource.password={blah}

spring.mail.username={blah@blah.com}
spring.mail.password={blah}
'''

Change SeedData.java to specify whatever user information you want. 
