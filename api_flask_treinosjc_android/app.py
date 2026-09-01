import os

from flask import Flask
from flask_migrate import Migrate

from db import db


def create_app(config_overrides=None):
    """Cria e configura a aplicação Flask (application factory).

    Passar `config_overrides` (um dict) permite trocar as configurações,
    por exemplo apontar para um banco SQLite em memória durante os testes.
    """
    app = Flask(__name__)

    app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
    app.config["SQLALCHEMY_DATABASE_URI"] = os.environ.get(
        "DATABASE_URL",
        "mysql+pymysql://root:root@localhost:3307/academia_db",
    )

    if config_overrides:
        app.config.update(config_overrides)

    # Importa os modelos para que o SQLAlchemy registre as tabelas
    # antes de qualquer chamada a db.create_all().
    from models import Usuario, Treino, Exercicio, HistoricoTreino  # noqa: F401

    db.init_app(app)
    Migrate(app, db)

    from routes.usuario import usuario_bp
    from routes.treino import treino_bp
    from routes.exercicio import exercicio_bp
    from routes.vinculo import vinculo_bp
    from routes.historico import historico_bp

    app.register_blueprint(usuario_bp)
    app.register_blueprint(treino_bp)
    app.register_blueprint(exercicio_bp)
    app.register_blueprint(vinculo_bp)
    app.register_blueprint(historico_bp)

    return app


# Instância padrão usada por `flask run` e pelo `flask db ...` (migrations).
app = create_app()


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
