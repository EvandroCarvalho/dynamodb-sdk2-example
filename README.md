# Exemplo de implementação simples utilizando o dynamodb jdk 2

partition = 10GB, 3000RCU e 1000 WCU

Tamanho json clients = 0,248Kb


### Como o DynamoDB aloca partições:
Partition = Round( RCU (PROV/3000 ) + WCU (PROV/1000) )


### CALCULO RCU
#### Reader
_Calculo Strongly consistent_

0,248Kb / 4Kb =  0,062 = 1 RCU

_Calculo Eventualy consistent_

0,248Kb / 1Kb = 0,248 = 1 RCU

#### Writer
0,248Kb / 1Kb = 0,248 = 1 WCU

### CALCULO PROVISIONADO
**Provisionamento dynamo (free tier usage limit) -> RCU = 5 e WCU = 5**

#### Reader - (as leitura são feitas em blocos de 4Kb)
_Calculo Strongly consistent_

4Kb x 5 = 20Kb => 20Kb / 0,248Kb =  80 items por segundo

_Calculo Eventualy consistent_

4Kb x 5 = 20Kb x 2 = 40Kb => 40Kb / 0,248 = 161 itens por segundo

#### Writer - blocos de 1Kb
1Kb x 5 = 5Kb
5Kb / 0,248Kb = 20 itens escritos por segundo

