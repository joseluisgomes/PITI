# Projeto Integrador em Telecomunicações e Informática
Protótipo que permite a transmissão de informação entre dois PCs sobre um *link* ótico.

## Autores

* [@joseluisgomes](https://github.com/joseluisgomes)
* [@ruipCunha0](https://github.com/ruipCunha0)
* [@diiogocerqueira](https://github.com/diiogocerqueira)
* [@Chiquinho2oo1](https://github.com/Chiquinho2oo1)
* [@luisandre-oliveira](https://github.com/luisandre-oliveira)

## Fases do projeto

- Fase **A** - Comunicação por fios entre placas ESP32.
    - As tarefas da fase A focão-se na transmissão unidirecional de dados (fluxo de *bits*) em sincronismo entre placas ESP32 (por fios: sinal e GND).

- Fase **B** - Comunicação ótica por infravermelhos entre placas ESP32.
    - As tarefas da fase B do projeto focam-se na implementação do *link* ótico para substituir os fios.
    - Inclui a implementação de mecanismos de sincronização/delimitação do fluxo de *bits* entre placas ESP32, a nível do *bit* do *byte* e da trama.
    - Esta fase inclui as seguintes tarefas principais:
        - Projeto do circuito **emissor** de infravermelhos para conversão do sinal elétrico extraído da porta série da estação transmissora em sinal ótico.
        - Projeto do circuito **recetor** para recuperação do sinal elétrico a partir do sinal ótico recebido e envio pela para porta série da estação recetora.
- Fase **C** - Sistema completo.
    - Implementação do controlo de erros para recuperar tramas perdidas/corrompidas e testes adicionais sobre o *link* ótico.
    - Implementação das aplicações de interface com o utilizador nos PCs.
    - Implementação de um protocolo de comunicação entre cada PC e a respetiva placa ESP32 (se necessário).
    - Integração e testes finais do sistema completo.

## Enquadramento do projeto

A figura abaixo ilustra uma visão geral da arquitetura do sistema completo. 

![diagramaFaseC](https://github.com/joseluisgomes/PITI/assets/70901488/819bbc87-a094-45d3-8747-8cbf816cf3f6)

Através da visualização da figura referida, a comunicação é estabelecida com recurso a um *link* ótico (representado pela seta amarela) que será gerado pelo LED. Posteriormente, o *link* ótico será detetado pelo fotodetetor, que é um componente intrínseco ao recetor e é responsável pela transformação
da informação presente no *link* ótico num sinal elétrico. 

De seguida, do lado do recetor, o PC irá executar uma aplicação que receberá e apresentará a informação correspondente.