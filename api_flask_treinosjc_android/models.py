from db import db
from datetime import datetime

usuario_treino = db.Table(
    "usuario_treino",
    db.Column("usuarioid", db.Integer, db.ForeignKey("usuario.id"), primary_key=True),
    db.Column("treinoid", db.Integer, db.ForeignKey("treino.id"), primary_key=True)
)

treino_exercicio = db.Table(
    "treino_exercicio",
    db.Column("treinoid", db.Integer, db.ForeignKey("treino.id"), primary_key=True),
    db.Column("exercicioid", db.Integer, db.ForeignKey("exercicio.id"), primary_key=True)
)


class Usuario(db.Model):
    __tablename__ = "usuario"

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    nome  = db.Column(db.String(100), nullable=False)
    email = db.Column(db.String(100), nullable=False)
    senha = db.Column(db.String(64), nullable=False, default='')
    role = db.Column(db.String(10), nullable=False, default='usuario') 

    treinos = db.relationship("Treino", secondary=usuario_treino, back_populates="usuarios")

    def para_dic(self):
        return {
            "id": self.id,
            "nome": self.nome,
            "email": self.email,
            "role": self.role
        }


class Treino(db.Model):
    __tablename__ = "treino"

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    nome = db.Column(db.String(100), nullable=False)
    descricao = db.Column(db.Text, nullable=False)

    usuarios = db.relationship("Usuario", secondary=usuario_treino, back_populates="treinos")
    exercicios = db.relationship("Exercicio", secondary=treino_exercicio, back_populates="treinos")

    def para_dic(self):
        return {
            "id": self.id,
            "nome": self.nome,
            "descricao": self.descricao
        }


class Exercicio(db.Model):
    __tablename__ = "exercicio"

    id  = db.Column(db.Integer, primary_key=True, autoincrement=True)
    nome = db.Column(db.String(100), nullable=False)
    series = db.Column(db.Integer, nullable=False)
    repeticoes = db.Column(db.Integer, nullable=False)

    treinos = db.relationship("Treino", secondary=treino_exercicio, back_populates="exercicios")

    def para_dic(self):
        return {
            "id": self.id,
            "nome": self.nome,
            "series": self.series,
            "repeticoes": self.repeticoes
        }



class HistoricoTreino(db.Model):
    __tablename__ = "historico_treino"

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    usuarioid = db.Column(db.Integer, db.ForeignKey("usuario.id", ondelete="CASCADE"), nullable=False)
    treinoid = db.Column(db.Integer, db.ForeignKey("treino.id", ondelete="CASCADE"), nullable=False)
    data = db.Column(db.DateTime, default=datetime.now)

    usuario = db.relationship("Usuario", backref=db.backref("historicos", passive_deletes=True))
    treino = db.relationship("Treino",  backref=db.backref("historicos", passive_deletes=True))

    def para_dic(self):
        return {
            "id": self.id,
            "usuarioId": self.usuarioid,
            "treinoId": self.treinoid,
            "treinoNome": self.treino.nome if self.treino else "",
            "data": self.data.strftime("%Y-%m-%d %H:%M:%S")
        }
