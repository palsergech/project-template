#!/usr/bin/env python3
"""
generate_users.py

Создаёт CSV с логинами и паролями и автоматически добавляет этих пользователей в Keycloak.

Пример:
    python3 gen_and_create_users.py \
        --count 5000 \
        --realm myrealm \
        --url https://keycloak.local \
        --admin admin \
        --password secret \
        --out users.csv
"""

import csv
import argparse
from pathlib import Path
import requests
import sys
from time import sleep

def get_admin_token(base_url: str, username: str, password: str) -> str:
    """Получить access_token для администратора"""
    url = f"{base_url}/realms/master/protocol/openid-connect/token"
    data = {
        "grant_type": "password",
        "client_id": "admin-cli",
        "username": username,
        "password": password,
    }
    resp = requests.post(url, data=data)
    resp.raise_for_status()
    return resp.json()["access_token"]

def create_user(base_url: str, realm: str, token: str, username: str, password: str):
    """Создать пользователя в Keycloak"""
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json",
    }
    url = f"{base_url}/admin/realms/{realm}/users"
    payload = {
        "username": username,
        "enabled": True,
        "emailVerified": True,
        "credentials": [
            {
                "type": "password",
                "value": password,
                "temporary": False,
            }
        ]
    }
    resp = requests.post(url, json=payload, headers=headers)
    if resp.status_code not in (201, 409):
        # 201 — OK, 409 — уже существует
        raise RuntimeError(f"Ошибка создания пользователя {username}: {resp.status_code} {resp.text}")

def generate_and_create(count: int, prefix: str, out_path: Path,
                        base_url: str, realm: str, admin: str, admin_pass: str,
                        start: int = 1, overwrite: bool = False):
    """Генерация CSV + создание пользователей в Keycloak"""
    if out_path.exists() and not overwrite:
        print(f"Файл {out_path} уже существует. Запустите с --overwrite, чтобы перезаписать.", file=sys.stderr)
        return 1

    token = get_admin_token(base_url, admin, admin_pass)
    print("✅ Получен токен администратора Keycloak")

    out_path.parent.mkdir(parents=True, exist_ok=True)

    created, skipped = 0, 0
    with out_path.open("w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        for i in range(start, start + count):
            username = f"{prefix}{i}"
            password = username
            try:
                create_user(base_url, realm, token, username, password)
                created += 1
            except Exception as e:
                skipped += 1
                print(f"⚠️  Ошибка при создании {username}: {e}")
            writer.writerow([username, password])

            # можно чуть "замедлить" при массовом создании
            if i % 100 == 0:
                print(f"Создано пользователей: {i - start + 1}/{count}")
                sleep(0.1)

    print(f"\n✅ Готово: {created} создано, {skipped} пропущено. CSV: {out_path}")
    return 0

def parse_args():
    p = argparse.ArgumentParser(description="Generate CSV and create users in Keycloak")
    p.add_argument("--count", type=int, default=5000)
    p.add_argument("--prefix", type=str, default="user")
    p.add_argument("--out", type=str, default="users.csv")
    p.add_argument("--start", type=int, default=1)
    p.add_argument("--overwrite", action="store_true")

    p.add_argument("--url", required=True, help="Базовый URL Keycloak, например: https://keycloak.local")
    p.add_argument("--realm", required=True, help="Realm, куда добавлять пользователей")
    p.add_argument("--admin", required=True, help="Admin username")
    p.add_argument("--password", required=True, help="Admin password")
    return p.parse_args()

def main():
    args = parse_args()
    out_path = Path(args.out)
    rc = generate_and_create(
        count=args.count,
        prefix=args.prefix,
        out_path=out_path,
        base_url=args.url.rstrip("/"),
        realm=args.realm,
        admin=args.admin,
        admin_pass=args.password,
        start=args.start,
        overwrite=args.overwrite
    )
    sys.exit(rc)

if __name__ == "__main__":
    main()
