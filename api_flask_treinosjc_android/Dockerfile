FROM python:3.12-slim

WORKDIR /app

# Instala as dependências primeiro (aproveita o cache de camadas do Docker)
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt gunicorn

# Copia o restante do código
COPY . .

EXPOSE 5000

# Sobe a API com um servidor WSGI de produção
CMD ["gunicorn", "--bind", "0.0.0.0:5000", "app:app"]
