from flask import Blueprint, jsonify, request
from db import db
from models import HistoricoTreino

historico_bp = Blueprint('historico', __name__)

@historico_bp.route("/finalizarTreino", methods=["POST"])
def finalizar_treino():
    usuario_id = request.form.get("txtUsuarioId", 0)
    treino_id = request.form.get("txtTreinoId", 0)

    if not usuario_id or not treino_id:
        return jsonify({"INSERT": "Erro", "MSG": "IDs inválidos"}), 400
    try:
        novo = HistoricoTreino(usuarioid=int(usuario_id), treinoid=int(treino_id))
        db.session.add(novo)
        db.session.commit()
        return jsonify({"INSERT": "OK", "id": novo.id, "data": novo.data.strftime("%Y-%m-%d %H:%M:%S")}), 201
    except Exception as e:
        return jsonify({"INSERT": "Erro", "MSG": str(e)}), 500


@historico_bp.route("/listarHistorico", methods=["GET"])
def listar_historico():
    usuario_id = request.args.get("txtUsuarioId", 0)

    if not usuario_id:
        return jsonify({"ERRO": "usuarioId inválido"}), 400

    try:
        historicos = HistoricoTreino.query\
            .filter_by(usuarioid=int(usuario_id))\
            .order_by(HistoricoTreino.data.desc())\
            .all()
        return jsonify([h.para_dic() for h in historicos]), 200
    except Exception as e:
        return jsonify({"ERRO": str(e)}), 500


@historico_bp.route("/historicoSemana", methods=["GET"])
def historico_semana():
    usuario_id = request.args.get("txtUsuarioId", 0)

    if not usuario_id:
        return jsonify({"ERRO": "usuarioId invalido"}), 400
    try:
        from datetime import datetime, timedelta
        hoje = datetime.now().date()
        inicio = hoje - timedelta(days=(hoje.weekday() + 1) % 7)
        fim = inicio + timedelta(days=6)
        historicos = HistoricoTreino.query.filter(
            HistoricoTreino.usuarioid == int(usuario_id),
            HistoricoTreino.data >= datetime.combine(inicio, datetime.min.time()),
            HistoricoTreino.data <= datetime.combine(fim, datetime.max.time())
        ).all()
        dias_treinados = list({(h.data.weekday() + 1) % 7 for h in historicos})
        return jsonify({"diasTreinados": dias_treinados,"total":len(historicos)}), 200
    except Exception as e:
        return jsonify({"ERRO": str(e)}), 500


@historico_bp.route("/historicoMes", methods=["GET"])
def historico_mes():
    usuario_id = request.args.get("txtUsuarioId", 0)

    if not usuario_id:
        return jsonify({"ERRO": "usuarioId inválido"}), 400
    try:
        from datetime import datetime
        hoje = datetime.now()
        ano = hoje.year
        mes = hoje.month
        historicos = HistoricoTreino.query.filter(
            HistoricoTreino.usuarioid == int(usuario_id),
            db.extract('year',  HistoricoTreino.data) == ano, db.extract('month', HistoricoTreino.data) == mes
        ).all()
        dias_treinados = list({h.data.day for h in historicos})

        return jsonify({ "ano": ano, "mes": mes, "diasTreinados": sorted(dias_treinados)}), 200
    except Exception as e:
        return jsonify({"ERRO": str(e)}), 500
