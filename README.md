# starwarsapi
API para gerenciamento de planetas do universo Star Wars

Utilização

Adicionar um planeta

Endpoint: /api/starwars/planeta/adicionar

Método: POST

Enviar no corpo da requisição um objeto Json no seguinte formato:

{
	"nome": "nome",
	"clima": "clima",
	"terreno": "terreno"
}

-----------------

Remover um planeta

Endpoint: /api/starwars/planeta/remover/{id_planeta}

Método: DELETE

-----------------

Buscar planetas com filtro

Endpoint: /api/starwars/planeta/buscar?nome={nome_planeta}

OBS: O parâmetro 'nome' é opcional, não informando-o a api retornará todos os planetas cadastrados.

Método: GET

-----------------

Buscar planeta

Endpoint: /api/starwars/planeta/{id_planeta}

Método: GET

-----------------

May the force be with you! :)
