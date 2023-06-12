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

Através da visualização da figura referida, a comunicação é estabelecida com recurso a um *link* ótico (representado pela seta amarela) que será gerado pelo **LED**. Posteriormente, o *link* ótico será detetado pelo fotodetetor, que é um componente intrínseco ao recetor e é responsável pela transformação
da informação presente no *link* ótico num sinal elétrico. 

De seguida, do lado do recetor, o PC irá executar uma aplicação que receberá e apresentará a informação correspondente.

## Circuito Emissor

A figura abaixo ilustra o circuito **emissor** implementado pelo grupo. 

Para implementar o circuito emissor foi necessário relembrar certos conceitos do domínio da eletrónica, em especial, o modo de funcionamento do transístor bipolar. O circuito emissor, através de um emissor de infravermelhos (LED), transmite o sinal elétrico, oriundo da porta série da estação emissora.

<p align="center">
  <img width="700" height="250" src="https://github.com/joseluisgomes/PITI/assets/70901488/c4b00be9-2f39-4510-aee3-08edddae7b44">
</p>

## Circuito Recetor

A figura 3.8 ilustra o circuito recetor implementado pelo grupo. Para implementar o circuito recetor também foi necessário relembrar certos conceitos do domínio da eletrónica, em especial, o modo de funcionamento dos AMPOPs.

O circuito recetor recupera o sinal elétrico através do sinal ótico e envia o mesmo pela porta série da estação recetora. Este circuito pode ser dividido em 4 partes: filtro passa-alto; amplificador de transimpedância; comparador de tensão e retificador de meia onda.

<p align="center">
  <img width="700" height="250" src="https://github.com/joseluisgomes/PITI/assets/70901488/7ef894f0-16d7-43e4-ab22-50f888cecf77">
</p>

## Interface gráfica da aplicação

A implementação das aplicações de interface gráfica com o utilizador nos PCs, inclui 3 menus:
- Menu **Principal**: Para escolha entre os restantes menus, tanto como o *baudrate* e a porta a usar.
- Menu **Recetor**: Permite fazer ações relacionadas ao recetor.
- Menu **Emissor**: Permite fazer ações relacionadas com o emissor.

No menu principal, o utilizador tem de selecionar a porta COM por onde pretende comunicar, e a *baudrate* que pretende usar. Caso algum destes campos não tenha sido preenchido, o programa gera um alerta de erro, onde avisa o campo que está por preencher.

A figura abaixo apresenta o menu principal em diferentes fases de execução.

![main_menu_empty](https://github.com/joseluisgomes/PITI/assets/70901488/f36b89f0-416e-4a3e-9ce0-3c81e29dd9b3)