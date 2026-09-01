"""Testes unitários para as rotas de usuário (cadastro, login, listagem)."""


def cadastrar_usuario(client, nome="Joao", email="joao@teste.com", senha="123456"):
    return client.post("/cadastrar", data={
        "txtNome": nome,
        "txtEmail": email,
        "txtSenha": senha,
    })


def test_cadastrar_usuario_com_sucesso(client):
    resp = cadastrar_usuario(client)
    assert resp.status_code == 201
    data = resp.get_json()
    assert data["INSERT"] == "OK"
    assert "id" in data


def test_cadastrar_usuario_sem_campos_obrigatorios(client):
    resp = client.post("/cadastrar", data={"txtNome": "Joao"})
    assert resp.status_code == 400
    assert resp.get_json()["INSERT"] == "Erro"


def test_cadastrar_usuario_email_duplicado(client):
    cadastrar_usuario(client, email="duplicado@teste.com")
    resp = cadastrar_usuario(client, nome="Outro", email="duplicado@teste.com")
    assert resp.status_code == 409
    assert resp.get_json()["INSERT"] == "Erro"


def test_login_com_credenciais_corretas(client):
    cadastrar_usuario(client, email="login@teste.com", senha="senha123")
    resp = client.post("/login", data={"txtEmail": "login@teste.com", "txtSenha": "senha123"})
    assert resp.status_code == 200
    data = resp.get_json()
    assert data["LOGIN"] == "OK"
    assert data["email"] == "login@teste.com"


def test_login_com_senha_incorreta(client):
    cadastrar_usuario(client, email="login2@teste.com", senha="senha123")
    resp = client.post("/login", data={"txtEmail": "login2@teste.com", "txtSenha": "errada"})
    assert resp.status_code == 401
    assert resp.get_json()["LOGIN"] == "Erro"


def test_login_sem_campos(client):
    resp = client.post("/login", data={})
    assert resp.status_code == 400


def test_listar_usuarios(client):
    cadastrar_usuario(client, nome="Bia", email="bia@teste.com")
    cadastrar_usuario(client, nome="Ana", email="ana@teste.com")
    resp = client.get("/listarUsuarios")
    assert resp.status_code == 200
    nomes = [u["nome"] for u in resp.get_json()]
    assert nomes == sorted(nomes)
    assert "Bia" in nomes and "Ana" in nomes


def test_alterar_usuario(client):
    cadastrado = cadastrar_usuario(client, email="alterar@teste.com").get_json()
    resp = client.post("/altUsuario", data={
        "txtId": cadastrado["id"],
        "txtNome": "Nome Novo",
        "txtEmail": "alterar@teste.com",
    })
    assert resp.status_code == 200
    assert resp.get_json()["UPDATE"] == "OK"


def test_excluir_usuario(client):
    cadastrado = cadastrar_usuario(client, email="excluir@teste.com").get_json()
    resp = client.post("/excUsuario", data={"txtId": cadastrado["id"]})
    assert resp.status_code == 200
    assert resp.get_json()["DELETE"] == "OK"

    resp_listar = client.get("/listarUsuarios")
    ids = [u["id"] for u in resp_listar.get_json()]
    assert cadastrado["id"] not in ids