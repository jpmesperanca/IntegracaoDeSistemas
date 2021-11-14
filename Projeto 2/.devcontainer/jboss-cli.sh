#Goto WildFly Home
cd $WILDFLY_HOME/bin
 
#Open JBOSS Command Line Tool
./jboss-cli.sh
 
#Define a outbound-socket-binding named “MyMailSMTP”


/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=MyMailSMTP:add( \
host=smtp.sendgrid.net, \
port=465)


#Define a JavaMail session named “MyMail”

/subsystem=mail/mail-session=MyMail:add(jndi-name="java:jboss/mail/gmail", from="integracaodesistemas2021@gmail.com", debug=true)

#Add a reference from “MyMail” to “MyMailSMTP”

/subsystem=mail/mail-session=MyMail/server=smtp:add( \
outbound-socket-binding-ref=MyMailSMTP, \
ssl=true, \
username=sendgrid_username, \
password=sendgrid_password)
 
#Reload Setting to reflect new changes
reload