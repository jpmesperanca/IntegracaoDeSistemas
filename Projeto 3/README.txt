Notas iniciais:
	- Visual Studio Code
	- Docker

1 - Através do VSCode
	1.1 - F1 -> "Remote-Containers: Open Folder in Container..." -> Abrir a pasta "Projeto 3" num container
	1.1.1 - Se o Wildfly não der start -> verificar line endings (CRLF -> LF)

2 - Instalar os jar em falta: 
	-> kafka-connect-jdbc-10.2.5.jar
	-> postgresql-42.3.1.jar
	
	2.1 - colocar os ficheiros .jar na pasta "Projeto 3"
	2.2 - dentro do containter, no diretorio "/workspace" executar os seguintes comandos:
		cp -v kafka-connect-jdbc-10.2.5.jar ../opt/kafka_2.13-2.8.1/libs
		cp -v postgresql-42.3.1.jar ../opt/kafka_2.13-2.8.1/libs

3 - Adicionar os 6 ficheiros de Properties dos Kafka Connectors:
	-> connect-jdbc-source-filipe.properties
	-> connect-jdbc-sink-filipe.properties
	-> connect-jdbc-sink-results.properties
	-> connect-jdbc-source-curr.properties
	-> connect-standalone-curr.properties
	-> connect-standalone-results.properties
	
	3.1 - colocar os ficheiros .properties na pasta "Projeto 3"
	3.2 - dentro do containter, no diretorio "/workspace" executar os seguintes comandos:
		cp -v connect-jdbc-source-filipe.properties ../opt/kafka_2.13-2.8.1/config
		cp -v connect-jdbc-sink-filipe.properties ../opt/kafka_2.13-2.8.1/config
		cp -v connect-jdbc-source-curr.properties ../opt/kafka_2.13-2.8.1/config
		cp -v connect-standalone-curr.properties ../opt/kafka_2.13-2.8.1/config
		cp -v connect-jdbc-sink-results.properties ../opt/kafka_2.13-2.8.1/config
		cp -v connect-standalone-results.properties ../opt/kafka_2.13-2.8.1/config


4 - Arrancar tudo (consolas diferentes). Dentro do containter, no diretorio "/workspace" executar, ***por esta ordem***:
		4.1 ---------- Consola 1 ----------
			cd jeeapp
			mvn clean install -U
			mvn wildfly:deploy

		4.2 ---------- Consola 2 ----------> Zookeper
			cd ../opt/kafka_2.13-2.8.1
			bin/zookeeper-server-start.sh config/zookeeper.properties

		4.3 ---------- Consola 3 ----------> Server
			cd ../opt/kafka_2.13-2.8.1
			bin/kafka-server-start.sh config/server.properties

		4.4 ---------- Consola 4 ----------> Kafka Connect 1
			cd ../opt/kafka_2.13-2.8.1
			bin/connect-standalone.sh config/connect-standalone.properties config/connect-jdbc-source-filipe.properties config/connect-jdbc-sink-filipe.properties

		4.5 ---------- Consola 5 ----------> Kafka Connect 2 
			cd ../opt/kafka_2.13-2.8.1
			bin/connect-standalone.sh config/connect-standalone-curr.properties config/connect-jdbc-source-curr.properties

		4.6 ---------- Consola 6 ----------> Kafka Connect 3
			cd ../opt/kafka_2.13-2.8.1
			bin/connect-standalone.sh config/connect-standalone-results.properties config/connect-jdbc-sink-results.properties
			
		4.7 ---------- CLIs ----------
			-> Começar Cliente da REST (Administrator CLI) -> "/RestClient/src/main/java/book/App.java"
			-> Criar alguns dados de teste através da consola disponível (opções 1, 2 e 3). Pelo menos 1 cliente, 1 manager e 1 currency.

			-> Começar simulador de clientes (gerador de pagamentos e créditos) -> "/KafkaClient/src/main/java/kafka/Client.java"
			-> Dar update através das opções 3 e 4 da CLI.
			-> Gerar créditos e pagamentos à vontade (ou pelo menos um de cada para que os tópicos sejam criados e o Kafka Streams consiga ter acesso aos mesmos)

			-> Começar Streams -> "/KafkaStreams/src/main/java/streams/Stream.java"

			-> Agora na Administrator CLI podemos utilizar as funções 7 a 18 (que estiverem implementadas)

		4.Extra:
			-> Pode dar jeito criar consumer simples apenas para ver o progresso de um certo tópico.
				Tópico Results -> bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic Results --from-beginning

5 - Terminar o projeto de forma segura
	-> Terminar o processo Streams (CTRL+C)
	-> Selecionar opção 19 no Administrator CLI -> "/RestClient/src/main/java/book/App.java"
	-> Selecionar opção 5 no Client Generator -> "/KafkaClient/src/main/java/kafka/Client.java"
	-> Terminar o processo Kafka Connect 3 (CTRL+C)
	-> Terminar o processo Kafka Connect 2 (CTRL+C)
	-> Terminar o processo Kafka Connect 1 (CTRL+C)
	-> Terminar o processo Server (CTRL+C)
	-> Terminar o processo Zookeeper (CTRL+C)
	-> Terminar o Container (File > Close Remote Connection)
	



6 - EXTRA:

Delete dos tópicos:
cd ../opt/kafka_2.13-2.8.1/bin
./kafka-topics.sh --delete --topic Results --zookeeper 0.0.0.0:2181
./kafka-topics.sh --delete --topic Credits --zookeeper 0.0.0.0:2181
./kafka-topics.sh --delete --topic Payments --zookeeper 0.0.0.0:2181
./kafka-topics.sh --delete --topic DBInfoClients --zookeeper 0.0.0.0:2181
./kafka-topics.sh --delete --topic DBInfoCurr --zookeeper 0.0.0.0:2181
./kafka-topics.sh --delete --topic Totais --zookeeper 0.0.0.0:2181

Delete de informação nas KafkaStreams:
cd ../opt/kafka_2.13-2.8.1/bin
./kafka-streams-application-reset.sh --application-id kafkastreamsBPU
./kafka-streams-application-reset.sh --application-id kafkastreamsPPU
./kafka-streams-application-reset.sh --application-id kafkastreamsCPU
./kafka-streams-application-reset.sh --application-id kafkastreamsTB
./kafka-streams-application-reset.sh --application-id kafkastreamsTP
./kafka-streams-application-reset.sh --application-id kafkastreamsTC
./kafka-streams-application-reset.sh --application-id kafkastreamsBwTW
./kafka-streams-application-reset.sh --application-id kafkastreamsPwTW
