import hashlib
from flask import Blueprint, jsonify, request
from db import db
from models import Usuario

usuario_bp = Blueprint('usuario', __name__)


def criptografar_senha(senha):
    return hashlib.sha256(senha.encode()).hexdigest()


@usuario_bp.route("/login", methods=["POST"])
def login():
    email = request.form.get("txtEmail", "").strip()
    senha = request.form.get("txtSenha", "").strip()

    if not email or not senha:
        return jsonify({"LOGIN": "Erro", "MSG": "Email e senha obrigatórios"}), 400
    usuario = Usuario.query.filter_by(
        email=email,
        senha=criptografar_senha(senha)
    ).first()
    if not usuario:
        return jsonify({"LOGIN": "Erro", "MSG": "Email ou senha inválidos"}), 401
    return jsonify({
        "LOGIN": "OK", "id": usuario.id, "nome": usuario.nome, "email":usuario.email,"role": usuario.role }), 200




@usuario_bp.route("/cadastrar", methods=["POST"])
def cadastrar():
    nome = request.form.get("txtNome", "").strip()
    email = request.form.get("txtEmail", "").strip()
    senha = request.form.get("txtSenha", "").strip()
    if not nome or not email or not senha:
        return jsonify({"INSERT": "Erro", "MSG": "Campos obrigatórios faltando"}), 400
    if Usuario.query.filter_by(email=email).first():
        return jsonify({"INSERT": "Erro", "MSG": "Email já cadastrado"}), 409

    novo = Usuario(nome=nome, email=email, senha=criptografar_senha(senha), role='usuario')
    db.session.add(novo)
    db.session.commit()
    return jsonify({"INSERT": "OK", "id": novo.id}), 201



@usuario_bp.route("/listarUsuarios", methods=["GET"])
def listar_usuarios():
    usuarios = Usuario.query.order_by(Usuario.nome).all()
    return jsonify([u.para_dic() for u in usuarios]), 200


@usuario_bp.route("/insUsuario", methods=["POST"])
def inserir_usuario():
    nome = request.form.get("txtNome", "").strip()
    email = request.form.get("txtEmail", "").strip()
    senha = request.form.get("txtSenha", "").strip()
    role = request.form.get("txtRole", "usuario").strip()

    if not nome or not email or not senha:
        return jsonify({"INSERT": "Erro", "MSG": "Campos obrigatórios faltando"}), 400
    if Usuario.query.filter_by(email=email).first():
        return jsonify({"INSERT": "Erro", "MSG": "Email já cadastrado"}), 409

    novo = Usuario(nome=nome, email=email, senha=criptografar_senha(senha), role=role)
    db.session.add(novo)
    db.session.commit()
    return jsonify({"INSERT": "OK", "id": novo.id}), 201


@usuario_bp.route("/altUsuario", methods=["POST"])
def alterar_usuario():
    id_usuario = request.form.get("txtId", 0)
    nome = request.form.get("txtNome", "").strip()
    email = request.form.get("txtEmail", "").strip()
    senha = request.form.get("txtSenha", "").strip()

    if not id_usuario or not nome or not email:
        return jsonify({"UPDATE": "Erro", "MSG": "Campos obrigatórios faltando"}), 400

    usuario = Usuario.query.get_or_404(id_usuario)
    usuario.nome = nome
    usuario.email = email
    if senha:
        usuario.senha = criptografar_senha(senha)
    db.session.commit()
    return jsonify({"UPDATE": "OK"}), 200


@usuario_bp.route("/excUsuario", methods=["POST"])
def excluir_usuario():
    id_usuario = request.form.get("txtId", 0)
    if not id_usuario:
        return jsonify({"DELETE": "Erro", "MSG": "ID invalido"}), 400
    usuario = Usuario.query.get_or_404(id_usuario)
    usuario.treinos.clear()  
    db.session.delete(usuario)
    db.session.commit()
    return jsonify({"DELETE": "OK"}), 200
