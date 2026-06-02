from flask import Blueprint, jsonify, request
from db import db
from models import Usuario, Treino

vinculo_bp = Blueprint('vinculo', __name__)


@vinculo_bp.route("/listarVinculos", methods=["GET"])
def listar_vinculos():
    usuarios = Usuario.query.all()
    lista = []
    for u in usuarios:
        for t in u.treinos:
            lista.append({
                "usuarioId": u.id,
                "usuarioNome": u.nome,
                "treinoId": t.id,
                "treinoNome": t.nome
            })
    return jsonify(lista), 200


@vinculo_bp.route("/vincularUsuarioTreino", methods=["POST"])
def vincular_usuario_treino():
    usuario_id = request.form.get("txtUsuarioId", 0)
    treino_id  = request.form.get("txtTreinoId", 0)

    if not usuario_id or not treino_id:
        return jsonify({"INSERT": "Erro", "MSG": "IDs inválidos"}), 400

    usuario = Usuario.query.get_or_404(usuario_id)
    treino  = Treino.query.get_or_404(treino_id)
    if treino not in usuario.treinos:
        usuario.treinos.append(treino)
        db.session.commit()

    return jsonify({"INSERT": "OK"}), 200


@vinculo_bp.route("/desvincularUsuarioTreino", methods=["POST"])
def desvincular_usuario_treino():
    usuario_id = request.form.get("txtUsuarioId", 0)
    treino_id  = request.form.get("txtTreinoId", 0)

    if not usuario_id or not treino_id:
        return jsonify({"DELETE": "Erro", "MSG": "IDs inválidos"}), 400

    usuario = Usuario.query.get_or_404(usuario_id)
    treino  = Treino.query.get_or_404(treino_id)
    if treino in usuario.treinos:
        usuario.treinos.remove(treino)
        db.session.commit()

    return jsonify({"DELETE": "OK"}), 200
