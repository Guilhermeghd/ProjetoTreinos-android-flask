from flask import Blueprint, jsonify, request
from db import db
from models import Exercicio, Treino

exercicio_bp = Blueprint('exercicio', __name__)


@exercicio_bp.route("/listarExercicios", methods=["GET"])
def listar_exercicios():
    exercicios = Exercicio.query.order_by(Exercicio.nome).all()
    return jsonify([e.para_dic() for e in exercicios]), 200


@exercicio_bp.route("/listarExerciciosPorTreino", methods=["GET"])
def listar_exercicios_por_treino():
    treino_id = request.args.get("txtTreinoId", 0)
    if not treino_id:
        return jsonify({"ERRO": "treinoId inválido"}), 400
    treino = Treino.query.get_or_404(treino_id)
    return jsonify([e.para_dic() for e in treino.exercicios]), 200



@exercicio_bp.route("/insExercicio", methods=["POST"])
def inserir_exercicio():
    nome = request.form.get("txtNome","").strip()
    series = request.form.get("txtSeries", 0)
    repeticoes = request.form.get("txtRepeticoes", 0)
    treino_id = request.form.get("txtTreinoId", 0)

    if not nome or not series or not repeticoes:
        return jsonify({"INSERT": "Erro", "MSG": "Campos obrigatorios faltando"}), 400
    if Exercicio.query.filter_by(nome=nome).first():
        return jsonify({"INSERT": "Erro", "MSG": "Ja existe um exercicio com esse nome"}), 409
    novo = Exercicio(nome=nome, series=int(series), repeticoes=int(repeticoes))
    db.session.add(novo)

    if treino_id and int(treino_id) > 0:
        treino = Treino.query.get(treino_id)
        if treino:
            treino.exercicios.append(novo)
    db.session.commit()
    return jsonify({"INSERT": "OK", "id": novo.id}), 201




@exercicio_bp.route("/altExercicio", methods=["POST"])
def alterar_exercicio():
    id_exercicio = request.form.get("txtId", 0)
    nome = request.form.get("txtNome", "").strip()
    series = request.form.get("txtSeries", 0)
    repeticoes = request.form.get("txtRepeticoes", 0)

    if not id_exercicio or not nome or not series or not repeticoes:
        return jsonify({"UPDATE": "Erro", "MSG": "Campos obrigatorios faltando"}), 400
    existente = Exercicio.query.filter_by(nome=nome).first()
    if existente and existente.id != int(id_exercicio):
        return jsonify({"UPDATE": "Erro", "MSG": "Ja existe um exercicio com esse nome"}), 409
    exercicio = Exercicio.query.get_or_404(id_exercicio)
    exercicio.nome = nome
    exercicio.series = int(series)
    exercicio.repeticoes = int(repeticoes)
    db.session.commit()
    return jsonify({"UPDATE": "OK"}), 200



@exercicio_bp.route("/excExercicio", methods=["POST"])
def excluir_exercicio():
    id_exercicio = request.form.get("txtId", 0)
    if not id_exercicio:
        return jsonify({"DELETE": "Erro", "MSG": "ID inválido"}), 400
    exercicio = Exercicio.query.get_or_404(id_exercicio)
    if exercicio.treinos:
        return jsonify({"DELETE": "Erro", "MSG": "Nao foi possivel excluir este exercicio pois tem treinos vinculado a esse exercicios"}), 409
    db.session.delete(exercicio)
    db.session.commit()
    return jsonify({"DELETE": "OK"}), 200


@exercicio_bp.route("/atualizarVinculosTreino", methods=["POST"])
def atualizar_vinculos_treino():
    treino_id = request.form.get("txtTreinoId", 0)
    exercicios_ids = request.form.get("txtExercicioIds", "") 
    if not treino_id:
        return jsonify({"UPDATE": "Erro", "MSG": "treinoId inválido"}), 400
    try:
        treino = Treino.query.get_or_404(treino_id)
        treino.exercicios.clear()
        if exercicios_ids:
            ids = [int(i) for i in exercicios_ids.split(",") if i.strip().isdigit()]
            for eid in ids:
                exercicio = Exercicio.query.get(eid)
                if exercicio:
                    treino.exercicios.append(exercicio)
        db.session.commit()
        return jsonify({"UPDATE": "OK"}), 200
    except Exception as e:
        return jsonify({"UPDATE": "Erro", "MSG": str(e)}), 500
