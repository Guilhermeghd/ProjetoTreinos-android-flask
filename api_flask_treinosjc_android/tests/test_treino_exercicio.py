"""Testes unitários para as rotas de treino e exercício, e o vínculo entre eles."""


def inserir_treino(client, nome="Treino A", descricao="Treino de peito"):
    return client.post("/insTreino", data={"txtNome": nome, "txtDescricao": descricao})


def inserir_exercicio(client, nome="Supino", series=3, repeticoes=12, treino_id=None):
    data = {"txtNome": nome, "txtSeries": series, "txtRepeticoes": repeticoes}
    if treino_id:
        data["txtTreinoId"] = treino_id
    return client.post("/insExercicio", data=data)


def test_inserir_treino_com_sucesso(client):
    resp = inserir_treino(client)
    assert resp.status_code == 201
    assert resp.get_json()["INSERT"] == "OK"


def test_inserir_treino_nome_duplicado(client):
    inserir_treino(client, nome="Treino Duplicado")
    resp = inserir_treino(client, nome="Treino Duplicado")
    assert resp.status_code == 409


def test_listar_treinos(client):
    inserir_treino(client, nome="Treino B")
    resp = client.get("/listarTreinos")
    assert resp.status_code == 200
    assert any(t["nome"] == "Treino B" for t in resp.get_json())


def test_inserir_exercicio_vinculado_a_treino(client):
    treino = inserir_treino(client, nome="Treino Pernas").get_json()
    resp = inserir_exercicio(client, nome="Agachamento", treino_id=treino["id"])
    assert resp.status_code == 201

    exercicios = client.get(
        f"/listarExerciciosPorTreino?txtTreinoId={treino['id']}"
    ).get_json()
    assert any(e["nome"] == "Agachamento" for e in exercicios)


def test_inserir_exercicio_nome_duplicado(client):
    inserir_exercicio(client, nome="Rosca Direta")
    resp = inserir_exercicio(client, nome="Rosca Direta")
    assert resp.status_code == 409


def test_excluir_exercicio_vinculado_falha(client):
    treino = inserir_treino(client, nome="Treino Costas").get_json()
    exercicio = inserir_exercicio(client, nome="Remada", treino_id=treino["id"]).get_json()

    resp = client.post("/excExercicio", data={"txtId": exercicio["id"]})
    assert resp.status_code == 409


def test_excluir_treino_sem_usuarios_vinculados(client):
    treino = inserir_treino(client, nome="Treino Livre").get_json()
    resp = client.post("/excTreino", data={"txtId": treino["id"]})
    assert resp.status_code == 200
    assert resp.get_json()["DELETE"] == "OK"