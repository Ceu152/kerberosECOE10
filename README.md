# kerberosECOE10
Segundo trabalho de ECOE10.


Kerberos é o nome de um Protocolo de rede, que permite comunicações individuais seguras e identificadas, em uma rede insegura. Para isso o Massachusetts Institute of Technology (MIT) disponibiliza um pacote de aplicativos que implementam esse protocolo. O protocolo Kerberos previne Eavesdropping e Replay attack, e ainda garante a integridade dos dados. Seus projetistas inicialmente o modelaram na arquitetura cliente-servidor, e é possível a autenticação mutua entre o cliente e o servidor, permitindo assim que ambos se autentiquem. 


Na pasta JARbins é onde ficam os arquivos refentes ao Key Distribution Center, adquirido no link: https://directory.apache.org/kerby/downloads.html

Na pasta keytab é onde ficam os arquivos key para o servidor KDC
A pasta workdir é a pasta de trabalho do servidor KDC
Na pasta aplicação de teste, existe o projeto em NETBEANS que simulam o funcionamento de um servidor e um cliente qualquer. O cliente se comunica com KDC para pedir o ticket de serviço. O servidor, abre o arquivo com o ticket (o equivalente a receber o ticket pela internet), e se comunica de volta com o KDC, para verificar se o cliente pode, de fato, acessar o serviço, e então toma a decisão de criar ou não o contexto seguro 

# Nota

## Feedback
Faltou detalhar mais o README e a escolha do caso de uso.

Critérios 
1. README [3]
  * Detalhamento - 0.4
  * Informações do caso de uso escolhido - 0.5
  * Informações da configuração - 0.6
2. Commits [3] - 2.6 - Bom, com participaçao dos outros membros. 
3. .gitignore [1] - 0
4. Código [3] - 2.6
5. Pontos extras
  * Extras (POC, GIF, Video, esforço) - 0
  * Organização - 0.3

Total = 7.0
(Não é necessariamente a nota final)



