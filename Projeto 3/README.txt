README

- Abrir a pasta "Projeto 3" num containter no docker
- Verificar line endings (CRLF -> LF) se o Wildfly não der start
- Instalar os jar em falta: 
	- kafka-connect-jdbc-10.2.5.jar
	- postgresql-42.3.1.jar
	
	- colocar os ficheiros .jar na pasta "Projeto 3"
	- dentro do containter, no diretorio "/workspace" executar 	
		cp -v kafka-connect-jdbc-10.2.5.jar ../opt/kafka_2.13-2.8.1/libs
		cp -v postgresql-42.3.1.jar ../opt/kafka_2.13-2.8.1/libs

- Adicionar os ficheiros properties para os Kafka Connectors
	- connect-jdbc-source-filipe.properties
	- connect-jdbc-sink-filipe.properties
	- connect-jdbc-source-curr.properties
	- connect-standalone-curr.properties
	
	- colocar os ficheiros .properties na pasta "Projeto 3"
	- dentro do containter, no diretorio "/workspace" executar
		cp -v connect-jdbc-source-filipe.properties ../opt/kafka_2.13-2.8.1/config
		cp -v connect-jdbc-sink-filipe.properties ../opt/kafka_2.13-2.8.1/config
		cp -v connect-jdbc-source-curr.properties ../opt/kafka_2.13-2.8.1/config
		cp -v connect-standalone-curr.properties ../opt/kafka_2.13-2.8.1/config

- Arrancar tudo (consolas diferentes). Dentro do containter, no diretorio "/workspace" executar, por esta ordem:
		1 ----------
		cd jeeapp
		mvn clean install -U
		mvn wildfly:deploy

		2 ----------
		cd ../opt/kafka_2.13-2.8.1
		bin/zookeeper-server-start.sh config/zookeeper.properties

		3 ----------
		cd ../opt/kafka_2.13-2.8.1
		bin/kafka-server-start.sh config/server.properties

		4 ----------
		cd ../opt/kafka_2.13-2.8.1
		bin/connect-standalone.sh config/connect-standalone.properties config/connect-jdbc-source-filipe.properties config/connect-jdbc-sink-filipe.properties

		5 ----------
		cd ../opt/kafka_2.13-2.8.1
		bin/connect-standalone.sh config/connect-standalone-curr.properties config/connect-jdbc-source-curr.properties

		6 ----------
		-> Começar Cliente da REST (Administrator CLI) -> "/KafkaStreams/src/main/java/streams/Stream.java"
		-> Criar alguns dados de teste através da consola disponível (opções 1, 2 e 3). Pelo menos 1 cliente, 1 manager e 1 currency.

		-> Começar simulador de clientes (gerador de pagamentos e créditos) -> "/KafkaClient/src/main/java/kafka/Client.java"
		-> Dar update através das opções 3 e 4 da CLI.
		-> Gerar créditos e pagamentos à vontade (ou pelo menos um de cada para que os tópicos sejam criados e o Kafka Streams consiga ter acesso aos mesmos)

		-> Começar Streams -> "/KafkaStreams/src/main/java/streams/Stream.java"


Extra:
Pode dar jeito criar consumer simples apenas para ver o progresso de um certo tópico.

- Tópico Results ->  bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic Results --from-beginning


Delete dos tópicos:
cd ../opt/kafka_2.13-2.8.1/bin
./kafka-streams-application-reset.sh --application-id kafkastreamsBPU
./kafka-streams-application-reset.sh --application-id kafkastreamsPPU
./kafka-streams-application-reset.sh --application-id kafkastreamsCPU
./kafka-streams-application-reset.sh --application-id kafkastreamsTB
./kafka-streams-application-reset.sh --application-id kafkastreamsTP
./kafka-streams-application-reset.sh --application-id kafkastreamsTC

