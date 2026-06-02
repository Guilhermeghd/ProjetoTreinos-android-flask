from flask import Blueprint, jsonify, request
from db import db
from models import Usuario, Treino

treino_bp = Blueprint('treino', __name__)


@treino_bp.route("/listarTreinos", methods=["GET"])
def listar_treinos():
    treinos = Treino.query.order_by(Treino.nome).all()
    return jsonify([t.para_dic() for t in treinos]), 200


@treino_bp.route("/listarTreinosPorUsuario", methods=["GET"])
def listar_treinos_por_usuario():
    usuario_id = request.args.get("txtUsuarioId", 0)
    if not usuario_id:
        return jsonify({"ERRO": "usuarioId inválido"}), 400
    usuario = Usuario.query.get_or_404(usuario_id)
    return jsonify([t.para_dic() for t in usuario.treinos]), 200



@treino_bp.route("/insTreino", methods=["POST"])
def inserir_treino():
    nome = request.form.get("txtNome","").strip()
    descricao = request.form.get("txtDescricao", "").strip()

    if not nome or not descricao:
        return jsonify({"INSERT": "Erro", "MSG": "Campos obrigatorios faltando"}), 400
    if Treino.query.filter_by(nome=nome).first():
        return jsonify({"INSERT": "Erro", "MSG": "Ja existe um treino com esse nome"}), 409
    novo = Treino(nome=nome, descricao=descricao)
    db.session.add(novo)
    db.session.commit()
    return jsonify({"INSERT": "OK", "id": novo.id}), 201




@treino_bp.route("/altTreino", methods=["POST"])
def alterar_treino():
    id_treino = request.form.get("txtId", 0)
    nome      = request.form.get("txtNome", "").strip()
    descricao = request.form.get("txtDescricao", "").strip()

    if not id_treino or not nome or not descricao:
        return jsonify({"UPDATE": "Erro", "MSG": "Campos obrigatorios faltando"}), 400
    existente = Treino.query.filter_by(nome=nome).first()
    if existente and existente.id != int(id_treino):
        return jsonify({"UPDATE": "Erro", "MSG": "Ja existe um treino com esse nome"}), 409
    treino = Treino.query.get_or_404(id_treino)
    treino.nome = nome
    treino.descricao = descricao
    db.session.commit()
    return jsonify({"UPDATE": "OK"}), 200


@treino_bp.route("/excTreino", methods=["POST"])
def excluir_treino():
    id_treino = request.form.get("txtId", 0)
    if not id_treino:
        return jsonify({"DELETE": "Erro", "MSG": "ID inválido"}), 400
    treino = Treino.query.get_or_404(id_treino)
    if treino.usuarios:
        return jsonify({"DELETE": "Erro", "MSG": "Nao foi possivel excluir este treino pois tem usuarios vinculados"}), 409
    treino.exercicios.clear()
    db.session.delete(treino)
    db.session.commit()
    return jsonify({"DELETE": "OK"}), 200


@treino_bp.route("/verificarTreinosDuplicados", methods=["POST"])
def verificar_treinos_duplicados():
    exercicio_ids = request.form.get("txtExercicioIds", "")
    treino_id_atual = request.form.get("txtTreinoId", 0)

    if not exercicio_ids:
        return jsonify({"duplicado": False}), 200
    ids_enviados = set(
        int(i) for i in exercicio_ids.split(",") if i.strip().isdigit()
    )
    if not ids_enviados:
        return jsonify({"duplicado": False}), 200
    treinos = Treino.query.all()
    for treino in treinos:
        if int(treino.id) == int(treino_id_atual):
            continue
        ids_treino = set(e.id for e in treino.exercicios)
        if ids_enviados == ids_treino:
            return jsonify({"duplicado":  True,"treinoNome": treino.nome,"MSG": "O treino \"" + treino.nome + "\" ja possui exatamente os mesmos exercicios selecionados"}), 200

    return jsonify({"duplicado": False}), 200
