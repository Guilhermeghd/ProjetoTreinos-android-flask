"""Testes unitários para o vínculo entre usuário e treino."""


def cadastrar_usuario(client, nome="Carla", email="carla@teste.com"):
    return client.post("/cadastrar", data={
        "txtNome": nome, "txtEmail": email, "txtSenha": "123456",
    }).get_json()


def inserir_treino(client, nome="Treino Vinculo"):
    return client.post("/insTreino", data={
        "txtNome": nome, "txtDescricao": "Descricao qualquer",
    }).get_json()


def test_vincular_usuario_treino(client):
    usuario = cadastrar_usuario(client)
    treino = inserir_treino(client)

    resp = client.post("/vincularUsuarioTreino", data={
        "txtUsuarioId": usuario["id"], "txtTreinoId": treino["id"],
    })
    assert resp.status_code == 200
    assert resp.get_json()["INSERT"] == "OK"

    vinculos = client.get("/listarVinculos").get_json()
    assert any(
        v["usuarioId"] == usuario["id"] and v["treinoId"] == treino["id"]
        for v in vinculos
    )


def test_desvincular_usuario_treino(client):
    usuario = cadastrar_usuario(client, email="desvinc@teste.com")
    treino = inserir_treino(client, nome="Treino Desvinculo")
    client.post("/vincularUsuarioTreino", data={
        "txtUsuarioId": usuario["id"], "txtTreinoId": treino["id"],
    })

    resp = client.post("/desvincularUsuarioTreino", data={
        "txtUsuarioId": usuario["id"], "txtTreinoId": treino["id"],
    })
    assert resp.status_code == 200

    vinculos = client.get("/listarVinculos").get_json()
    assert not any(
        v["usuarioId"] == usuario["id"] and v["treinoId"] == treino["id"]
        for v in vinculos
    )


def test_vincular_com_ids_invalidos(client):
    resp = client.post("/vincularUsuarioTreino", data={})
    assert resp.status_code == 400