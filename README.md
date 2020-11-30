########### exemplo de implementação simples utilizando o dynamodb jdk 2 ############
Partition - 10GB, 3000RCU e 1000 WCU
#Como o DynamoDB aloca partições:
Partition = Round( RCU (PROV/3000 ) + WCU (PROV/1000) )

#######################################
tamanho json = 0,248Kb


########## CALCULO RCU #################
#Reader
Strongly consistent
0,248Kb / 4Kb =  0,062 = 1 RCU

Eventualy consistent
0,248Kb / 1Kb = 0,248 = 1 RCU

#Writer
0,248Kb / 1Kb = 0,248 = 1 WCU

########## CALCULO PROVISIONADO ##################
Provisionamento default dynamo -> RCU = 5 e WCU = 5

#Reader - blocos de 4Kb
Strongly consistent
4Kb x 5 = 20Kb => 20Kb / 0,248Kb =  80 items por segundo

Eventualy consistent
4Kb x 5 = 20Kb x 2 = 40Kb => 40Kb / 0,248 = 161 itens por segundo

#Writer - blocos de 1Kb
1Kb x 5 = 5Kb
5Kb / 0,248Kb = 20 itens escritos por segundo

