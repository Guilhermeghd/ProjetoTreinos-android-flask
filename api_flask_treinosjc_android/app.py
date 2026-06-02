from flask import Flask
from flask_migrate import Migrate
from db import db
from routes.usuario import usuario_bp
from routes.treino import treino_bp
from routes.exercicio import exercicio_bp
from routes.vinculo import vinculo_bp
from routes.historico import historico_bp

app = Flask(__name__)

app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
app.config["SQLALCHEMY_DATABASE_URI"] = 'mysql+pymysql://root:root@localhost:3307/academia_db'

db.init_app(app)
migrate = Migrate(app, db)

app.register_blueprint(usuario_bp)
app.register_blueprint(treino_bp)
app.register_blueprint(exercicio_bp)
app.register_blueprint(vinculo_bp)
app.register_blueprint(historico_bp)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
