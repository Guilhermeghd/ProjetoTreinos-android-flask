import os
import sys

# Garante que os módulos da API (app, db, models, routes...) sejam
# importáveis quando o pytest é executado a partir de qualquer diretório.
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

import pytest

from app import create_app
from db import db as _db


@pytest.fixture()
def app():
    """Cria uma instância do Flask app com um banco SQLite em memória,
    isolada do MySQL usado em desenvolvimento/produção."""
    app = create_app({
        "TESTING": True,
        "SQLALCHEMY_DATABASE_URI": "sqlite:///:memory:",
    })

    with app.app_context():
        _db.create_all()
        yield app
        _db.session.remove()
        _db.drop_all()


@pytest.fixture()
def client(app):
    return app.test_client()


@pytest.fixture()
def db(app):
    return _db